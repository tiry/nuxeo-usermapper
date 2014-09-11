package org.nuxeo.usermapper.extension;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class GroovyUserMapper extends AbstractUserMapper {

    protected final String scriptSource;

    protected GroovyClassLoader loader;

    protected Class<?> groovyClass;

    public GroovyUserMapper(String script) {
        super();
        this.scriptSource = script;
    }

    @Override
    public void init(Map<String, String> params) {
        loader = new GroovyClassLoader(this.getClass().getClassLoader());
        groovyClass = loader.parseClass(scriptSource);        
    }

    @Override
    protected void getUserAttributes(Object userObject,
            Map<String, Serializable> attributes) {

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("attributes", attributes);
        context.put("userObject", userObject);
        Binding binding = new Binding(context);
        Script script = InvokerHelper.createScript(groovyClass, binding);
        script.run();
    }

    @Override
    public Object wrapNuxeoPrincipal(NuxeoPrincipal principal) {
        throw new UnsupportedOperationException(
                "The Groovy mapper does not support wrapping");
    }

    @Override
    public void release() {
        loader.clearCache();
        try {
            loader.close();
        } catch (IOException e) {
            log.error("Error during Groovy cleanup", e);
        }
    }

}
