package org.nuxeo.usermapper.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.usermapper.extension.GroovyUserMapper;
import org.nuxeo.usermapper.extension.UserMapper;

@XObject("mapper")
public class UserMapperDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XNode("@name")
    protected String name;
            
    @XNode("@class")
    Class<UserMapper> mapperClass;
    
    @XNodeMap(value = "parameters/parameter", key = "@name", type = HashMap.class, componentType = String.class)
    protected Map<String, String> params;

    
    @XNode("script")
    protected String sourceScript;
    
    
    public UserMapper getInstance() throws Exception {        
        UserMapper mapper = null;
        
        // use groovy impl as fallback
        if (mapperClass==null || mapperClass.getCanonicalName().equals(GroovyUserMapper.class.getCanonicalName())) {
            mapper = new GroovyUserMapper(sourceScript);
        } else {
            mapper = mapperClass.newInstance();
        }        
        
        // run init
        mapper.init(params);
        
        return mapper;
    }
    
}
