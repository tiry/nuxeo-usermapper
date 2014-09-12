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

package org.nuxeo.usermapper.extension;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

/**
 * Provide default implementation for interaction with the {@link UserManager}.
 * 
 * @author tiry
 *
 */
public abstract class AbstractUserMapper implements UserMapper {

    protected static final Log log = LogFactory.getLog(AbstractUserMapper.class);

    protected final UserManager userManager;

    public AbstractUserMapper() {
        userManager = Framework.getService(UserManager.class);
    }

    @Override
    public NuxeoPrincipal getCreateOrUpdateNuxeoPrincipal(Object userObject) {

        DocumentModel userModel = null;
        Map<String, Serializable> attributes = new HashMap<String, Serializable>();
        getUserAttributes(userObject, attributes);

        String userId = (String) attributes.get(userManager.getUserIdField());

        if (userId != null) {
            userModel = userManager.getUserModel(userId);
        }
        if (userModel == null) {
            if (attributes.size() > 0) {
                DocumentModelList userDocs = userManager.searchUsers(
                        attributes, Collections.<String> emptySet());
                if (userDocs.size() > 1) {
                    log.warn("Can not map user with filter "
                            + attributes.toString() + " : too many results");
                }
                if (userDocs.size() == 1) {
                    userModel = userDocs.get(0);
                }
            }
        }
        if (userModel != null) {
            updatePrincipal(attributes, userModel);
        } else {
            userModel = createPrincipal(attributes);
        }

        if (userModel != null) {
            userId = (String) userModel.getPropertyValue(userManager.getUserIdField());
            return userManager.getPrincipal(userId);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void updatePrincipal(Map<String, Serializable> attributes,
            DocumentModel userModel) {
        userModel.getDataModel(userManager.getUserSchemaName()).setMap(
                (Map) attributes);
        userManager.updateUser(userModel);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected DocumentModel createPrincipal(Map<String, Serializable> attributes) {

        DocumentModel userModel = userManager.getBareUserModel();
        userModel.getDataModel(userManager.getUserSchemaName()).setMap(
                (Map) attributes);
        return userManager.createUser(userModel);
    }

    protected abstract void getUserAttributes(Object userObject,
            Map<String, Serializable> attributes);

}
