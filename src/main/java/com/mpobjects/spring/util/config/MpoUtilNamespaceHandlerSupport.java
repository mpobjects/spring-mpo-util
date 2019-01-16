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
		protected void doParse(Element aElement, ParserContext aParserContext, BeanDefinitionBuilder aBuilder) {
			List parsedList = aParserContext.getDelegate().parseListElement(aElement, aBuilder.getRawBeanDefinition());
			aBuilder.addPropertyValue("locations", parsedList);

			String groups = aElement.getAttribute("groups");
			if (StringUtils.hasText(groups)) {
				aBuilder.addPropertyValue("groups", groups);
			}

			String groupsRef = aElement.getAttribute("groups-ref");
			if (StringUtils.hasText(groupsRef)) {
				aBuilder.addPropertyValue("groupsRef", groupsRef);
			}

			if (!StringUtils.isEmpty(groups) && !StringUtils.isEmpty(groupsRef)) {
				aParserContext.getReaderContext().error("'groups' and 'groups-ref' cannot both be set.", aElement);
			}

			String pattern = aElement.getAttribute("pattern");
			if (StringUtils.hasText(pattern)) {
				aBuilder.addPropertyValue("pattern", pattern);
			}

			String groupIndex = aElement.getAttribute("group-index");
			if (StringUtils.hasText(groupIndex)) {
				aBuilder.addPropertyValue("groupIndex", groupIndex);
			}

			String scope = aElement.getAttribute("scope");
			if (StringUtils.hasLength(scope)) {
				aBuilder.setScope(scope);
			}
		}

		@Override
		protected Class getBeanClass(Element aElement) {
			return FilteredResourcesFactoryBean.class;
		}
	}

	@Override
	public void init() {
		registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
	}
}
