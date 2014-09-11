package org.nuxeo.usermapper.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.usermapper.extension.UserMapper;

public class UserMapperComponent extends DefaultComponent implements
        UserMapperService {

    protected Map<String, UserMapper> mappers = new HashMap<String, UserMapper>();

    protected List<UserMapperDescriptor> descriptors = new ArrayList<>();    
    
    public static final String MAPPER_EP = "mapper";

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (MAPPER_EP.equalsIgnoreCase(extensionPoint)) {
            UserMapperDescriptor desc = (UserMapperDescriptor) contribution;
            descriptors.add(desc);
        }
    }

    @Override
    public void applicationStarted(ComponentContext context) throws Exception {
        for (UserMapperDescriptor desc : descriptors) {
            mappers.put(desc.name, desc.getInstance());
        }
    }

    @Override
    public void unregisterContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (MAPPER_EP.equalsIgnoreCase(extensionPoint)) {
            UserMapperDescriptor desc = (UserMapperDescriptor) contribution;
            UserMapper um = mappers.get(desc.name);
            if (um != null) {
                um.release();
                mappers.remove(desc.name);
            }
            mappers.put(desc.name, desc.getInstance());
        }
    }

    @Override
    public void deactivate(ComponentContext context) throws Exception {

        for (UserMapper um : mappers.values()) {
            um.release();
        }
        super.deactivate(context);
    }

    public UserMapper getMapper(String mappingName) throws ClientException {
        UserMapper um = mappers.get(mappingName);
        if (um == null) {
            throw new ClientException("No mapping defined for " + mappingName);
        }
        return um;
    }

    @Override
    public NuxeoPrincipal getCreateOrUpdateNuxeoPrincipal(String mappingName,
            Object userObject) throws ClientException {
        return getMapper(mappingName).getCreateOrUpdateNuxeoPrincipal(
                userObject);
    }

    @Override
    public Object wrapNuxeoPrincipal(String mappingName,
            NuxeoPrincipal principal) throws ClientException {
        return getMapper(mappingName).wrapNuxeoPrincipal(principal);
    }

    @Override
    public Set<String> getAvailableMappings() {
        return mappers.keySet();
    }

}
