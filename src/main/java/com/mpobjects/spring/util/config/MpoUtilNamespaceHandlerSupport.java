/*
 * Copyright 2019, MP Objects, http://www.mp-objects.com
 */
package com.mpobjects.spring.util.config;

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.mpobjects.spring.util.FilteredResourcesFactoryBean;

/**
 *
 */
public class MpoUtilNamespaceHandlerSupport extends NamespaceHandlerSupport {

	public static class ResourcesBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
		@Override
		protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
			List parsedList = parserContext.getDelegate().parseListElement(element, builder.getRawBeanDefinition());
			builder.addPropertyValue("locations", parsedList);

			String groups = element.getAttribute("groups");
			if (StringUtils.hasText(groups)) {
				builder.addPropertyValue("groups", groups);
			}

			String pattern = element.getAttribute("pattern");
			if (StringUtils.hasText(pattern)) {
				builder.addPropertyValue("pattern", pattern);
			}

			String groupIndex = element.getAttribute("group-index");
			if (StringUtils.hasText(groupIndex)) {
				builder.addPropertyValue("groupIndex", groupIndex);
			}

			String scope = element.getAttribute("scope");
			if (StringUtils.hasLength(scope)) {
				builder.setScope(scope);
			}
		}

		@Override
		protected Class getBeanClass(Element element) {
			return FilteredResourcesFactoryBean.class;
		}
	}

	@Override
	public void init() {
		registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
	}
}
