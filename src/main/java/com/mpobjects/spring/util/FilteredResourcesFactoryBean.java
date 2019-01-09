/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mpobjects.spring.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.StringUtils;

/**
 * Filters (and sorts) resources according to a group specification.
 */
public class FilteredResourcesFactoryBean extends AbstractFactoryBean<Resource[]> implements ResourceLoaderAware {

	private static final int NOT_FOUND = Integer.MAX_VALUE;

	private int groupIndex = 1;

	private String groups;

	private List<String> locations;

	private Pattern pattern;

	private ResourcePatternResolver resourcePatternResolver;

	public FilteredResourcesFactoryBean() {
		resourcePatternResolver = new PathMatchingResourcePatternResolver();
	}

	@Override
	public Class<? extends Resource[]> getObjectType() {
		return Resource[].class;
	}

	public void setGroups(String aGroups) {
		groups = aGroups;
	}

	public void setLocations(List<String> aLocations) {
		locations = aLocations;
	}

	public void setPattern(Pattern aPattern) {
		pattern = aPattern;
	}

	@Override
	public void setResourceLoader(ResourceLoader aResourceLoader) {
		resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(aResourceLoader);
	}

	@Override
	protected Resource[] createInstance() throws Exception {
		final List<String> ordering = loadGroupOrder();

		List<Resource> result = new ArrayList<>();
		for (String location : locations) {
			List<Resource> resources = new ArrayList<>(Arrays.asList(resourcePatternResolver.getResources(location)));
			for (Resource resource : resources) {
				if (indexOf(resource, ordering) != NOT_FOUND) {
					result.add(resource);
				}
			}
		}

		Collections.sort(result, new Comparator<Resource>() {
			@Override
			public int compare(Resource r1, Resource r2) {
				return indexOf(r1, ordering) - indexOf(r2, ordering);
			}
		});
		return result.toArray(new Resource[result.size()]);
	}

	protected String getResourceName(Resource aResource) {
		return aResource.getFilename();
	}

	/**
	 * Return the group index.
	 *
	 * @param aResource
	 * @param aGroups
	 * @return The position in the list, or {@value #NOT_FOUND} if it was not present.
	 */
	protected int indexOf(Resource aResource, List<String> aGroups) {
		if (pattern == null || aGroups.isEmpty()) {
			return -1;
		}
		String name = getResourceName(aResource);
		Matcher m = pattern.matcher(name);
		if (!m.matches() || m.groupCount() < groupIndex) {
			return NOT_FOUND;
		}
		String group = m.group(groupIndex);
		int idx = aGroups.indexOf(group);
		if (idx == -1) {
			return NOT_FOUND;
		} else {
			return idx;
		}
	}

	protected List<String> loadGroupOrder() {
		if (groups == null) {
			return Collections.emptyList();
		}
		Resource resource = resourcePatternResolver.getResource(groups);
		if (resource == null) {
			throw new IllegalStateException(String.format("Failed to resolve resource %s", groups));
		}

		List<String> result = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!StringUtils.hasText(line) || line.startsWith("#")) {
					continue;
				}
				result.add(line);
			}
		} catch (IOException e) {
			throw new IllegalStateException(String.format("Failed to load resource %s", groups), e);
		}
		return result;
	}

}
