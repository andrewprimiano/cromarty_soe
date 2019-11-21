package com.cromarty.ignition;

import com.cromarty.ignition.event.EventConsumer;
import com.cromarty.ignition.soe.EventActionHandler;
import com.cromarty.ignition.soe.SOE_RPC;
import com.cromarty.ignition.soe.SOE_TagPropContributor;
import com.inductiveautomation.ignition.common.TagPathUtils;
import com.inductiveautomation.ignition.common.alarming.AlarmEvent;
import com.inductiveautomation.ignition.common.alarming.AlarmListener;
import com.inductiveautomation.ignition.common.browsing.BrowseFilter;
import com.inductiveautomation.ignition.common.browsing.Results;
import com.inductiveautomation.ignition.common.config.ConfigurationPropertyModel;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.project.*;
import com.inductiveautomation.ignition.common.project.resource.ProjectResource;
import com.inductiveautomation.ignition.common.project.resource.ResourcePath;
import com.inductiveautomation.ignition.common.project.resource.ResourceType;
import com.inductiveautomation.ignition.common.script.JythonExecException;
import com.inductiveautomation.ignition.common.script.builtin.PyArgumentMap;
import com.inductiveautomation.ignition.common.sqltags.model.TagProp;
import com.inductiveautomation.ignition.common.tags.model.event.TagChangeEvent;
import com.inductiveautomation.ignition.common.tags.model.event.InvalidListenerException;
import com.inductiveautomation.ignition.common.tags.model.event.TagChangeListener;
import com.inductiveautomation.ignition.common.sqltags.parser.TagPathComponent;
import com.inductiveautomation.ignition.common.tags.browsing.NodeAttribute;
import com.inductiveautomation.ignition.common.tags.browsing.NodeDescription;
import com.inductiveautomation.ignition.common.tags.model.TagPath;
import com.inductiveautomation.ignition.common.tags.model.TagProvider;
import com.inductiveautomation.ignition.common.tags.model.TagProviderInformation;
import com.inductiveautomation.ignition.common.tags.paths.parser.TagPathParser;
import com.inductiveautomation.ignition.common.xmlserialization.SerializationException;
import com.inductiveautomation.ignition.common.xmlserialization.deserialization.XMLDeserializer;
import com.inductiveautomation.ignition.gateway.alarming.AlarmManager;
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.alarming.common.ModuleMeta;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.common.QualifiedPath;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.gateway.model.GatewayModuleHook;

import com.inductiveautomation.ignition.gateway.tags.managed.ManagedTagProvider;
import org.python.core.*;
import soe.EventProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GatewayHook extends AbstractGatewayModuleHook implements EventConsumer<SOE_Event>, AlarmListener, ProjectResourceListener, ProjectListener, GatewayModuleHook, TagChangeListener {

    public static final String MODULE_ID = "com.cromarty.ignition.soe";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private volatile GatewayContext gatewayContext;
    private volatile AlarmManager alarmManager;
    private volatile QualifiedPath listenerPath;
    private EventActionHandler eventActionHandler;
    private ArrayList<SOE_Script> scripts;
    private ArrayList<Project> subscribedProjects = new ArrayList<>();
    private SOE_TagPropContributor tagPropContributor;
    private ManagedTagProvider ourProvider;

    private ConfigurationPropertyModel prop;
    private SOE_TagPropContributor contributor;

    @Override
    public void setup(GatewayContext gatewayContext) {

        this.gatewayContext = gatewayContext;
        this.loadScripts();

        BundleUtil.get().addBundle("EventProperties", getClass(), "EventProperties");

        QualifiedPath path = new QualifiedPath();
        QualifiedPath.Builder builder = new QualifiedPath.Builder();
        listenerPath = builder.build();

        alarmManager = gatewayContext.getAlarmManager();
        alarmManager.registerExtendedConfigProperties(ModuleMeta.MODULE_ID, EventProperties.EnableSOE);
        alarmManager.addListener(listenerPath,this);

        this.addProjectListeners();
        gatewayContext.getProjectManager().addProjectListener(this);

        contributor = new SOE_TagPropContributor();
        gatewayContext.getTagManager().getConfigManager().registerTagPropertyContributor(contributor);

        for (TagProvider provider:gatewayContext.getTagManager().getTagProviders())
        {
            try {
                TagPath myPath= TagPathParser.parse("["+provider.getName()+"]");

                Results<NodeDescription> tags = provider.browseAsync(myPath, new BrowseFilter()).get();

                ;
                for (NodeDescription tag:tags.getResults())
                {
//                    tag=tag.
                }

                List <TagPath> lista= new ArrayList<TagPath>();
                lista.add(myPath);
                
                
                
                gatewayContext.getTagManager().subscribeAsync(myPath, (TagChangeListener) this);
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startup(LicenseState licenseState) {

    }

    @Override
    public void shutdown() {
        alarmManager.removeListener(listenerPath, this);
        gatewayContext.getProjectManager().addProjectListener(this);
        BundleUtil.get().removeBundle("EventProperties");
        this.removeProjectListeners();
        gatewayContext.getTagManager().getConfigManager().unregisterTagPropertyContributor(contributor);
    }

    private void addProjectListeners()
    {
        for (String name:gatewayContext.getProjectManager().getProjectNames()) {
            Optional<RuntimeProject> proj = gatewayContext.getProjectManager().getProject(name);
            if(proj.isPresent()) {
                proj.get().addProjectResourceListener(this);
                this.subscribedProjects.add(proj.get());
            }

        }
    }

    private void removeProjectListeners()
    {
        for (Project proj:this.subscribedProjects) {
            proj.removeProjectResourceListener(this);
            gatewayContext.getProjectManager().removeProjectListener(this);
        }
    }

    @Override
    public void onActive(AlarmEvent alarmEvent) {
        if(alarmEvent.get(EventProperties.EnableSOE)) {
            this.runScripts(new SOE_Event(alarmEvent));
        }
    }

    @Override
    public void onClear(AlarmEvent alarmEvent) {

        if(alarmEvent.get(EventProperties.EnableSOE))
            this.runScripts(new SOE_Event(alarmEvent));
    }

    @Override
    public void onAcknowledge(AlarmEvent alarmEvent) {
        if(alarmEvent.get(EventProperties.EnableSOE))
            this.runScripts(new SOE_Event(alarmEvent));
    }

    @Override
    public void resourcesCreated(String s, List<ChangeOperation.CreateResourceOperation> list) {
        this.loadScripts();
    }

    @Override
    public void resourcesModified(String s, List<ChangeOperation.ModifyResourceOperation> list) {
        this.loadScripts();
    }

    @Override
    public void resourcesDeleted(String s, List<ChangeOperation.DeleteResourceOperation> list) {
        this.loadScripts();
    }

    @Override
    public void projectAdded(String s) {

        Optional<RuntimeProject> proj = gatewayContext.getProjectManager().getProject(s);
        if(proj.isPresent()) {
            proj.get().addProjectResourceListener(this);
            this.subscribedProjects.add(proj.get());
        }
    }

    @Override
    public void projectDeleted(String s) {
        Optional<RuntimeProject> proj = gatewayContext.getProjectManager().getProject(s);
        if(proj.isPresent()) {
            proj.get().removeProjectResourceListener(this);
            this.subscribedProjects.remove(proj.get());
        }
    }

    @Override
    public void projectUpdated(String s) {
        this.loadScripts();
    }

    @Override
    public Object getRPCHandler(ClientReqSession session, String projectId) {
        SOE_RPC RPC_Endpoint = new SOE_RPC();
        RPC_Endpoint.getEventProducer().addEventListener(this);
        return RPC_Endpoint;
    }



    private void runScripts(SOE_Event event)
    {
        for (SOE_Script script:scripts){
            if(script.getEnabled()) {
                try {
                    PyObject argMap[] = new PyObject[1];
                    String keywords[] = new String[1];
                    Class<SOE_Event>[] types = new Class[1];

                    argMap[0] = Py.java2py(event);
                    keywords[0] = "event";
                    types[0] = SOE_Event.class;
                    PyArgumentMap args = PyArgumentMap.interpretPyArgs(argMap, keywords, keywords, types);
                    gatewayContext.getScriptManager().runCode(script.getUserScript(), Py.java2py(args), null, null);
                } catch (JythonExecException e) {
                    logger.info(e.toString());
                }
            }
        }
    }

    private void loadScripts()
    {
        scripts = new ArrayList<>();

        XMLDeserializer deserializer = gatewayContext.createDeserializer();

        for (String name:gatewayContext.getProjectManager().getProjectNames())
        {
            Optional<RuntimeProject> proj = gatewayContext.getProjectManager().getProject(name);
            List res = proj.get().browse(new ResourcePath(new ResourceType("SOE Module", "SOE Script"), "")).orElseGet(Collections::emptyList);

            for (Object resource : res) {
                try {
                    logger.info(((ProjectResource)resource).getResourceName());
                    SOE_Script obj = (SOE_Script)XMLDeserializer.deserialize(deserializer, ((ProjectResource)resource).getData(), logger);
                    scripts.add(obj);


                } catch (SerializationException e) {
                    logger.info(e.toString());
                }


            }
        }
    }

    @Override
    public void onEventReceived(SOE_Event EventData) {
        this.runScripts(EventData);
    }


    @Override
    public void tagChanged(TagChangeEvent tagChangeEvent) throws InvalidListenerException {

    }
}

