package org.nuxeo.usermapper.extension;

import java.util.Map;

import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface UserMapper {

    NuxeoPrincipal getCreateOrUpdateNuxeoPrincipal(Object userObject);

    Object wrapNuxeoPrincipal(NuxeoPrincipal principal);

    void init(Map<String, String> params) throws Exception ;
    
    void release();
}
