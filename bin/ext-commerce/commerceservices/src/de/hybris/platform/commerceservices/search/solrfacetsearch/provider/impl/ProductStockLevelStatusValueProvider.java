/*
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
 *  
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.store.BaseStoreModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * This ValueProvider will provide the product stock level status. The stock level count changes so frequently that it
 * is not sensible to index the count directly, but rather to map the count to a status (or band) and then index the
 * status.
 */
public class ProductStockLevelStatusValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	private FieldNameProvider fieldNameProvider;
	private CommerceStockService commerceStockService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

			if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty()
					&& getCommerceStockService().isStockSystemEnabled(baseSiteModel.getStores().get(0)))
			{
				fieldValues.addAll(createFieldValue(product, baseSiteModel.getStores().get(0), indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, null, indexedProperty));
			}

			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get stock of non-product item");
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final BaseStoreModel baseStore,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (baseStore != null)
		{
			final StockLevelStatus stockLevelStatus = getProductStockLevelStatus(product, baseStore);
			if (stockLevelStatus != null)
			{
				addFieldValues(fieldValues, indexedProperty, stockLevelStatus);
			}
		}
		else
		{
			addFieldValues(fieldValues, indexedProperty, StockLevelStatus.INSTOCK);
		}

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected StockLevelStatus getProductStockLevelStatus(final ProductModel product, final BaseStoreModel baseStore)
	{
		return getCommerceStockService().getStockLevelStatusForProductAndBaseStore(product, baseStore);
	}
}
