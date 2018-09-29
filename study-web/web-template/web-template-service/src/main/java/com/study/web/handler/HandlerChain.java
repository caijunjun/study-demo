package com.study.web.handler;

import java.util.List;

import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * 泛型Handler链
 * 适用场景，后期需要对某个大方法进行重构的时候适用，前期、小方法重构没有任何意义
 * 
 * @author caijunjun
 * @date 2018年9月29日
 * @param <C>
 * @param <R>
 * @param <H>
 */
public class HandlerChain<C extends Context, R extends Result<R>, H extends Handler<C, R>> {

	private TransactionTemplate transactionTemplate;

	private List<H> handlerList;

	public HandlerChain(List<H> handlerList, TransactionTemplate transactionTemplate) {
		super();
		Assert.notEmpty(handlerList, "handler 集合不能为空");
		Assert.notNull(transactionTemplate, "事务模板 不能为空");
		this.handlerList = handlerList;
		this.transactionTemplate = transactionTemplate;
	}

	public R handler(C context, boolean isOpenTransaction) {
		// 当前是否存在事务了
		boolean isExistTransaction;
		try {
			TransactionAspectSupport.currentTransactionStatus();
			isExistTransaction = true;
		} catch (Exception e) {
			isExistTransaction = false;
		}
		if (isOpenTransaction && !isExistTransaction) {
			return transactionTemplate.execute(status -> handler(context));
		}
		return handler(context);

	}

	private R handler(C context) {
		R result = null;
		for (int i = 0; i < handlerList.size(); i++) {
			result = handlerList.get(i).handler(context, result);
			if (result != null && result.isFinish()) {
				break;
			}
		}
		return result;
	}

}
