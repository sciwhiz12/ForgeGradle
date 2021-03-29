/*
 * ForgeGradle
 * Copyright (C) 2018 Forge Development LLC
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package net.minecraftforge.gradle.mcp;

import javax.inject.Inject;

import net.minecraftforge.gradle.common.util.Artifact;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;

public class MCPExtension {
    public static final String EXTENSION_NAME = "mcp";

    protected final Property<Artifact> config;
    protected final Property<String> pipeline;

    @Inject
    public MCPExtension(ObjectFactory objects) {
        config = objects.property(Artifact.class);
        pipeline = objects.property(String.class);
    }

    public Provider<Artifact> getConfig() {
        return this.config;
    }

    public void setConfig(Provider<Artifact> value) {
        this.config.set(value);
    }

    public void setConfig(Artifact value) {
        this.config.set(value);
    }

    public void setConfig(String value) {
        if (value.indexOf(':') != -1) { // Full artifact
            config.set(Artifact.from(value));
        } else {
            config.set(Artifact.from("de.oceanlabs.mcp:mcp_config:" + value + "@zip"));
        }
    }

    public Provider<String> getPipeline() {
        return this.pipeline;
    }

    public void setPipeline(Provider<String> value) {
        this.pipeline.set(value);
    }

    public void setPipeline(String value) {
        this.pipeline.set(value);
    }
}
