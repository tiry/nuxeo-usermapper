package org.nuxeo.usermapper.test;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.usermapper.service.UserMapperService;
import org.nuxeo.usermapper.test.dummy.DummyUser;

@Deploy({ "org.nuxeo.usermapper", "org.nuxeo.ecm.platform.login",
    "org.nuxeo.ecm.platform.login.default"})

@RunWith(FeaturesRunner.class)
@LocalDeploy("org.nuxeo.usermapper:usermapper-contribs.xml")
@Features(PlatformFeature.class)
public class TestUserMapperService {

    @Test
    public void shouldDeclareService() throws Exception {        
        UserMapperService ums = Framework.getLocalService(UserMapperService.class);
        Assert.assertNotNull(ums);        
        Assert.assertEquals(1,  ums.getAvailableMappings().size());
    }

    @Test
    public void testJavaContrib() throws Exception {
        
        // test create
        DummyUser dm = new DummyUser("jchan","Jacky", "Chan");
        UserMapperService ums = Framework.getLocalService(UserMapperService.class);        
        NuxeoPrincipal principal = ums.getCreateOrUpdateNuxeoPrincipal("javaDummy", dm);                
        Assert.assertNotNull(principal);
        Assert.assertEquals("jchan", principal.getName());
        Assert.assertEquals("Jacky", principal.getFirstName());
        Assert.assertEquals("Chan", principal.getLastName());        
        
        // test update
        dm = new DummyUser("jchan",null, "Chan2");
        principal = ums.getCreateOrUpdateNuxeoPrincipal("javaDummy", dm);                
        Assert.assertNotNull(principal);
        Assert.assertEquals("jchan", principal.getName());
        Assert.assertEquals("Jacky", principal.getFirstName());
        Assert.assertEquals("Chan2", principal.getLastName());
        
    }               
    
}
