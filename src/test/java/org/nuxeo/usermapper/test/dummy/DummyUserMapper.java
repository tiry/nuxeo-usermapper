package org.nuxeo.usermapper.test.dummy;

import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.usermapper.extension.AbstractUserMapper;
import org.nuxeo.usermapper.extension.UserMapper;

public class DummyUserMapper extends AbstractUserMapper implements UserMapper {

    @Override
    public Object wrapNuxeoPrincipal(NuxeoPrincipal principal) {
        return null;
    }

    @Override
    public void init(Map<String, String> params) throws Exception {
    }

    @Override
    public void release() {
    }

    @Override
    protected void getUserAttributes(Object userObject,
            Map<String, Serializable> attributes) {        
        if (userObject instanceof DummyUser) {
            DummyUser du = (DummyUser)userObject;            
            attributes.put(userManager.getUserIdField(), du.login);
            if (du.getName().getFirstName()!=null) {
                attributes.put("firstName", du.getName().getFirstName());
            }
            if (du.getName().getLastName()!=null) {
                attributes.put("lastName", du.getName().getLastName());
            }
        }
    }

}
