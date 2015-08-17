/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.dao.impl;

import com.poka.dao.BaseDaoI;
import com.poka.util.HibernateSqlServerUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

/**
 *
 * @author poka
 */
public class BaseDaoSqlServer<T> implements BaseDaoI<T>{

  
	private SessionFactory sessionFactory = HibernateSqlServerUtil.getSessionFactory();
	
	/**
	 * 获得当前事物的session
	 * 
	 * @return org.hibernate.Session
	 */
	@Override
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Serializable save(T o) {
		if (o != null) {                      
			return getCurrentSession().save(o);
		}
		return null;
	}

	@Override
	public T getById(Class<T> c, Serializable id) {
		return (T) getCurrentSession().get(c, id);
	}

	@Override
	public T getByHql(String hql) {
		          Query q = getCurrentSession().createQuery(hql);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
                Transaction transaction = null;			                        
                transaction = getCurrentSession().beginTransaction();
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<T> l = q.list();
                transaction.commit();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public void delete(T o) {
		if (o != null) {
			getCurrentSession().delete(o);
		}
	}

	@Override
	public void update(T o) {
		if (o != null) {
                        Transaction  transaction = null;
                        transaction = getCurrentSession().beginTransaction();
			getCurrentSession().update(o);
                        transaction.commit();                                               
		}
	}

	@Override
	public void saveOrUpdate(T o) {			
		if (o != null) {
			getCurrentSession().saveOrUpdate(o);
		}
	}

	@Override
	public List<T> find(String hql) {
                Transaction  transaction = null;
                transaction = getCurrentSession().beginTransaction();
	        Query q = getCurrentSession().createQuery(hql);
                List<T> tem =q.list();
                transaction.commit();
		return tem;
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		Query q = getCurrentSession().createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public Long count(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return (Long) q.uniqueResult();
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return (Long) q.uniqueResult();
	}

	@Override
	public int executeHql(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.executeUpdate();
	}

	@Override
	public List<T> findBySql(String sql) {
		Transaction  transaction = null;
                transaction = getCurrentSession().beginTransaction();
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		List<T> myList = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
                transaction.commit();
                return myList;
	}

	@Override
	public List<T> findBySql(String sql, int page, int rows) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public List<T> findBySql(String sql, Map<String, Object> params) {
                Transaction  transaction = null;
                transaction = getCurrentSession().beginTransaction();
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
                List<T> list = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
                transaction.commit();
		return list;
	}

	@Override
	public List<T> findBySql(String sql, Map<String, Object> params, int page, int rows) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public int executeSql(String sql) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.executeUpdate();
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
                Transaction  transaction = null;
                transaction = getCurrentSession().beginTransaction();		
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}                
		int result = q.executeUpdate();
                transaction.commit();
		return result;
	}

	@Override
	public BigInteger countBySql(String sql) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		String result = q.uniqueResult().toString();
		BigInteger count = new BigInteger(result);
		return count;
	}

	@Override
	public BigInteger countBySql(String sql, Map<String, Object> params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		String result = q.uniqueResult().toString();
		BigInteger count = new BigInteger(result);
		return count;
	}

}
