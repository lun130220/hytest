/*
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.ychinaaccelerator.storefront.controllers.cms;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleSuggestionComponentModel;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSComponentService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.yacceleratorfacades.suggestion.SimpleSuggestionFacade;
import de.hybris.platform.ychinaaccelerator.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import junit.framework.Assert;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link CartSuggestionComponentController}
 */
@UnitTest
public class CartSuggestionComponentControllerTest
{
	private static final String COMPONENT_UID = "componentUid";
	private static final String TEST_COMPONENT_UID = "componentUID";
	private static final String TEST_TYPE_CODE = SimpleSuggestionComponentModel._TYPECODE;
	private static final String TEST_TYPE_VIEW = ControllerConstants.Views.Cms.ComponentPrefix
			+ StringUtils.lowerCase(TEST_TYPE_CODE);
	private static final String TITLE = "title";
	private static final String TITLE_VALUE = "Accessories";
	private static final String SUGGESTIONS = "suggestions";
	private static final String COMPONENT = "component";

	private CartSuggestionComponentController cartSuggestionComponentController;

	@Mock
	private CartSuggestionComponentModel cartSuggestionComponentModel;
	@Mock
	private Model model;
	@Mock
	private DefaultCMSComponentService cmsComponentService;
	@Mock
	private SimpleSuggestionFacade simpleSuggestionFacade;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private ProductData productData;

	private final List<ProductData> productDataList = Collections.singletonList(productData);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		cartSuggestionComponentController = new CartSuggestionComponentController();
		cartSuggestionComponentController.setCmsComponentService(cmsComponentService);
		ReflectionTestUtils
				.setField(cartSuggestionComponentController, "simpleSuggestionFacade", simpleSuggestionFacade);
	}

	@Test
	public void testRenderComponent() throws Exception
	{
		given(cartSuggestionComponentModel.getMaximumNumberProducts()).willReturn(Integer.valueOf(1));
		given(cartSuggestionComponentModel.getTitle()).willReturn(TITLE_VALUE);
		given(cartSuggestionComponentModel.getProductReferenceTypes()).willReturn(
				Arrays.asList(ProductReferenceTypeEnum.ACCESSORIES));
		given(Boolean.valueOf(cartSuggestionComponentModel.isFilterPurchased())).willReturn(Boolean.TRUE);

		given(simpleSuggestionFacade.getReferencesForProducts(Mockito.anySetOf(String.class), Mockito.anyList(),
						Mockito.anyBoolean(), Mockito.<Integer> any())).willReturn(productDataList);

		final String viewName = cartSuggestionComponentController.handleComponent(request, response, model,
				cartSuggestionComponentModel);
		verify(model, Mockito.times(1)).addAttribute(TITLE, TITLE_VALUE);
		verify(model, Mockito.times(1)).addAttribute(SUGGESTIONS, productDataList);
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test
	public void testRenderComponentUid() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(cartSuggestionComponentModel);
		given(cartSuggestionComponentModel.getMaximumNumberProducts()).willReturn(Integer.valueOf(1));
		given(cartSuggestionComponentModel.getTitle()).willReturn(TITLE_VALUE);
		given(cartSuggestionComponentModel.getProductReferenceTypes()).willReturn(
				Arrays.asList(ProductReferenceTypeEnum.ACCESSORIES));
		given(Boolean.valueOf(cartSuggestionComponentModel.isFilterPurchased())).willReturn(Boolean.TRUE);

		given(simpleSuggestionFacade.getReferencesForProducts(Mockito.anySetOf(String.class), Mockito.anyList(),
						Mockito.anyBoolean(), Mockito.<Integer> any())).willReturn(productDataList);

		final String viewName = cartSuggestionComponentController.handleGet(request, response, model);
		verify(model, Mockito.times(1)).addAttribute(COMPONENT, cartSuggestionComponentModel);
		verify(model, Mockito.times(1)).addAttribute(TITLE, TITLE_VALUE);
		verify(model, Mockito.times(1)).addAttribute(SUGGESTIONS, productDataList);
		Assert.assertEquals(TEST_TYPE_VIEW, viewName);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(null);
		cartSuggestionComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound2() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(null);
		given(request.getParameter(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		cartSuggestionComponentController.handleGet(request, response, model);
	}

	@Test(expected = AbstractPageController.HttpNotFoundException.class)
	public void testRenderComponentNotFound3() throws Exception
	{
		given(request.getAttribute(COMPONENT_UID)).willReturn(TEST_COMPONENT_UID);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willReturn(null);
		given(cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).willThrow(new CMSItemNotFoundException(""));
		cartSuggestionComponentController.handleGet(request, response, model);
	}
}
