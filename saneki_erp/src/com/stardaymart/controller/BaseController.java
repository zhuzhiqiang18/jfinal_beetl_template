package com.stardaymart.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;

public class BaseController<M extends Model<M>> extends Controller{
	  /**
     * 获取M的class
     *
     * @return M
     */
    @SuppressWarnings("unchecked")
    public Class<M> getClazz() {
        Type t = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) t).getActualTypeArguments();
        return (Class<M>) params[0];
    }

    protected Class<M> modelClass;

    public Class<M> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<M> modelClass) {
        this.modelClass = modelClass;
    }
}
