package org.nuxeo.usermapper.service;

import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface UserMapperService {

    NuxeoPrincipal getCreateOrUpdateNuxeoPrincipal(String mappingName, Object userObject) throws ClientException;

    Object wrapNuxeoPrincipal(String mappingName, NuxeoPrincipal principal) throws ClientException;
    
    Set<String> getAvailableMappings();
}
