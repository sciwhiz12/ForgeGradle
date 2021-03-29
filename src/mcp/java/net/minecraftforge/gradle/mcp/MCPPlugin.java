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

import net.minecraftforge.gradle.common.FGBasePlugin;
import net.minecraftforge.gradle.common.util.Utils;
import net.minecraftforge.gradle.mcp.task.DownloadMCPConfig;
import net.minecraftforge.gradle.mcp.task.SetupMCP;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository.MetadataSources;
import org.gradle.api.tasks.TaskProvider;

import javax.annotation.Nonnull;

public class MCPPlugin implements Plugin<Project> {

    @Override
    public void apply(@Nonnull Project project) {
        project.getPlugins().apply(FGBasePlugin.class);
        MCPExtension extension = project.getExtensions().create("mcp", MCPExtension.class, project);

        TaskProvider<DownloadMCPConfig> downloadConfig = project.getTasks().register("downloadConfig", DownloadMCPConfig.class);
        TaskProvider<SetupMCP> setupMCP = project.getTasks().register("setupMCP", SetupMCP.class);

        downloadConfig.configure(task -> {
            task.setConfig(extension.getConfig().toString());
            task.setOutput(project.file("build/mcp_config.zip"));
        });
        setupMCP.configure(task -> {
            task.dependsOn(downloadConfig);
            task.setPipeline(extension.getPipeline().get());
            task.setConfig(downloadConfig.get().getOutput());
        });
    }
}
