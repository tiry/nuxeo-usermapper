/*
 * (C) Copyright 2006-2014 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.nuxeo.usermapper.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.usermapper.extension.GroovyUserMapper;
import org.nuxeo.usermapper.extension.UserMapper;

/**
 * XMap descriptor for contributing {@link UserMapper} plugins
 * 
 * @author tiry
 *
 */
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
