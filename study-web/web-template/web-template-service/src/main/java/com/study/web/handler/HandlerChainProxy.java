package com.study.web.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

public class HandlerChainProxy<H> implements InvocationHandler {

	private TransactionTemplate transactionTemplate;

	private List<H> handlerList;

	private static final ThreadLocal<HandlerChainInfoHolder> chainInfoHolder = new NamedThreadLocal<>(
			"Current ChainInfoHolder");

	public HandlerChainProxy(List<H> handlerList, TransactionTemplate transactionTemplate) {
		super();
		Assert.notEmpty(handlerList, "handler 集合不能为空");
		Assert.notNull(transactionTemplate, "事务模板 不能为空");
		this.handlerList = handlerList;
		this.transactionTemplate = transactionTemplate;

	}

	public static HandlerChainInfoHolder currentChainInfoHolder() {
		HandlerChainInfoHolder holder = chainInfoHolder.get();
		if (holder == null) {
			holder = new HandlerChainInfoHolder();
			chainInfoHolder.set(holder);
		}
		return holder;
	}

	@SuppressWarnings("unchecked")
	public H getInstance() {
		Class<?> clazz = handlerList.get(0).getClass();
		return (H) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// 获取上下文信息
		boolean isOpenTransaction = currentChainInfoHolder().isOpenTransaction;

		if (isOpenTransaction) {
			// 当前是否存在事务了
			boolean isExistTransaction;
			try {
				TransactionAspectSupport.currentTransactionStatus();
				isExistTransaction = true;
			} catch (Exception e) {
				isExistTransaction = false;
			}
			// 如果当前不存在事务
			if (!isExistTransaction) {
				return transactionTemplate.execute(status -> {
					try {
						return invoke(method, args);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new IllegalArgumentException(e);
					}
				});
			}
		}
		Object invokeResult = invoke(method, args);
		chainInfoHolder.remove();
		return invokeResult;
	}

	private Object invoke(Method method, Object[] args)
			throws IllegalAccessException, InvocationTargetException {
		Result<?> result;
		Object invokeResult = null;
		for (int i = 0; i < handlerList.size(); i++) {
			result = currentChainInfoHolder().getResult();
			if (result != null && result.isFinish()) {
				break;
			}
			invokeResult = method.invoke(handlerList.get(i), args);
		}
		return invokeResult;
	}

	public static class HandlerChainInfoHolder {

		private Context context;

		private Result<?> result;

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public Result<?> getResult() {
			return result;
		}

		public void setResult(Result<?> result) {
			this.result = result;
		}

		private boolean isOpenTransaction;

		public boolean isOpenTransaction() {
			return isOpenTransaction;
		}

		public void setOpenTransaction(boolean isOpenTransaction) {
			this.isOpenTransaction = isOpenTransaction;
		}

	}
}
