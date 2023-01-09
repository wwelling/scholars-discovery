/*
 * Copyright 2016-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.rest.webmvc.json;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link HandlerMethodArgumentResolver} to resolve {@link Pageable} from a {@link PageableArgumentResolver} applying
 * field to property mapping.
 * <p>
 * A resolved {@link Pageable} is post-processed by applying Jackson field-to-property mapping if it contains a
 * {@link Sort} instance. Customized fields are resolved to their property names. Unknown properties are removed from
 * {@link Sort}.
 *
 * @author Mark Paluch
 * @author Oliver Gierke
 * @since 2.6
 */
public class MappingAwarePageableArgumentResolver implements PageableArgumentResolver {

	private final JacksonMappingAwareSortTranslator translator;
	private final PageableArgumentResolver delegate;

	public MappingAwarePageableArgumentResolver(JacksonMappingAwareSortTranslator translator,
			PageableArgumentResolver delegate) {

		Assert.notNull(translator, "JacksonMappingAwareSortTranslator must not be null");
		Assert.notNull(delegate, "Delegate PageableArgumentResolver must not be null");

		this.translator = translator;
		this.delegate = delegate;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return delegate.supportsParameter(parameter);
	}

	@Override
	public @NonNull Pageable resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {

		Pageable pageable = delegate.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

		if (pageable.isUnpaged() || pageable.getSort().isUnsorted()) {
			return pageable;
		}

		// The sort translate was removed due to incompatibility with our Individual entity not representing
		// one-to-one the Solr document properties. The pageable and sort arguments are proxied directly to Solr
		// where the documents will have the properties to sort where the entity manager is unaware of.
		// Sort translated = translator.translateSort(pageable.getSort(), methodParameter, webRequest);

		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
	}
}