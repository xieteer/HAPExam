/*
 * #{copyright}#
 */
package com.hand.hap.function.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hand.hap.cache.impl.HashStringRedisCacheGroup;
import com.hand.hap.cache.impl.RoleResourceCache;
import com.hand.hap.core.ILanguageProvider;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.function.dto.Function;
import com.hand.hap.function.dto.FunctionDisplay;
import com.hand.hap.function.dto.FunctionResource;
import com.hand.hap.function.dto.MenuItem;
import com.hand.hap.function.dto.Resource;
import com.hand.hap.function.mapper.FunctionMapper;
import com.hand.hap.function.mapper.FunctionResourceMapper;
import com.hand.hap.function.mapper.RoleFunctionMapper;
import com.hand.hap.function.service.IFunctionService;
import com.hand.hap.function.service.IResourceService;
import com.hand.hap.function.service.IRoleFunctionService;
import com.hand.hap.system.dto.Language;
import com.hand.hap.system.service.impl.BaseServiceImpl;

/**
 * 功能服务接口实现.
 * 
 * @author wuyichu
 */
@Transactional
@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function> implements IFunctionService {

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private FunctionResourceMapper functionResourceMapper;

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IRoleFunctionService roleFunctionService;

    @Autowired
    @Qualifier("functionCache")
    private HashStringRedisCacheGroup<Function> functionCache;

    @Autowired
    private RoleResourceCache roleResourceCache;

    @Autowired
    private ILanguageProvider languageProvider;

    /**
     * 新增功能.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            功能
     * @return 返回新增后的功能信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Function insertSelective(IRequest request, Function function) {
        if (function == null) {
            return null;
        }
        super.insertSelective(request, function);
        reloadFunctionCache(function.getFunctionId());
        return function;
    }

    private void reloadFunctionCache(Long functionId) {
        IRequest old = RequestHelper.getCurrentRequest();
        try {
            IRequest iRequest = RequestHelper.newEmptyRequest();
            RequestHelper.setCurrentRequest(iRequest);
            for (Language language : languageProvider.getSupportedLanguages()) {
                iRequest.setLocale(language.getLangCode());
                Function f = functionMapper.selectByPrimaryKey(functionId);
                if (f != null) {
                    functionCache.getGroup(language.getLangCode()).setValue(functionId.toString(), f);
                }
            }
        } finally {
            RequestHelper.setCurrentRequest(old);
        }
    }

    private void deleteFunctionCache(Long functionId) {
        for (Language language : languageProvider.getSupportedLanguages()) {
            functionCache.getGroup(language.getLangCode()).remove(functionId.toString());
        }
    }

    /**
     * 功能修改.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            功能
     * @return 修改后的功能信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Function updateByPrimaryKeySelective(IRequest request, Function function) {
        if (function == null) {
            return null;
        }
        super.updateByPrimaryKeySelective(request, function);
        reloadFunctionCache(function.getFunctionId());
        return function;
    }

    /**
     * 根据ID查询功能.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            function with ID
     * @return 功能信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Function selectByPrimaryKey(IRequest request, Function function) {
        if (function.getFunctionId() == null) {
            return null;
        }
        Function result = functionMapper.selectByPrimaryKey(function.getFunctionId());
        if (request != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("function", result);
            List<Resource> resources = functionMapper.selectExistsResourcesByFunction(params);
            result.setResources(resources);
        }
        return result;
    }

    /**
     * 删除功能.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            功能
     * @return 删除的行数
     */
    private int deleteByPrimaryKey(IRequest request, Function function) {
        if (function.getFunctionId() == null) {
            return 0;
        }
        functionResourceMapper.deleteByFunctionId(function.getFunctionId());
        roleFunctionMapper.deleteByFunctionId(function.getFunctionId());
        deleteFunctionCache(function.getFunctionId());
        return functionMapper.deleteByPrimaryKey(function);
    }

    /**
     * 批量新增或修改.
     * 
     * @param request
     *            上下文请求
     * @param functions
     *            功能集合
     * @return 修改或新增过后的功能信息集合
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Function> batchUpdate(IRequest request, List<Function> functions) {
        if (functions == null || functions.isEmpty()) {
            return functions;
        }
        for (Function function : functions) {
            if (function.getFunctionId() == null) {
                self().insertSelective(request, function);
            } else {
                self().updateByPrimaryKeySelective(request, function);
            }
        }
        return functions;
    }

    /**
     * 批量删除.
     * 
     * @param request
     *            上下文请求
     * @param functions
     *            功能集合
     * @return 删除的函数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchDelete(IRequest request, List<Function> functions) {
        int result = 0;
        if (functions == null || functions.isEmpty()) {
            return result;
        }

        for (Function function : functions) {
            deleteByPrimaryKey(request, function);
            result++;
        }
        return result;
    }

    /**
     * 根据功能条件查询.
     * 
     * @param request
     *            上下文请求
     * @param example
     *            请求参数
     * @param page
     *            页码
     * @param pageSize
     *            页数
     * @return 满足条件的功能
     */
    @Override
    public List<FunctionDisplay> selectFunction(IRequest request, Function example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<FunctionDisplay> list = functionMapper.selectFunctions(example);
        List<Function> allFunctions = functionCache.getGroup(request.getLocale()).getAll();
        Map<Long, Function> idFuncMap = new HashMap<>();
        allFunctions.forEach(f -> idFuncMap.put(f.getFunctionId(), f));
        list.forEach((function) -> {
            Function parent = idFuncMap.get(function.getParentFunctionId());
            if (parent != null) {
                function.setParentFunctionName(parent.getFunctionName());
            }
        });
        return list;
    }

    /**
     * 查询function挂靠的resource.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            功能
     * @param resource
     *            资源
     * @param page
     *            页码
     * @param pageSize
     *            页数
     * @return 满足条件的resource集合
     */
    @Override
    public List<Resource> selectExitResourcesByFunction(IRequest request, Function function, Resource resource,
            int page, int pageSize) {
        if (function == null || function.getFunctionId() == null) {
            return null;
        }
        PageHelper.startPage(page, pageSize);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("function", function);
        params.put("resource", resource);
        return functionMapper.selectExistsResourcesByFunction(params);
    }

    /**
     * 查询function没有挂靠的resource.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            功能
     * @param resource
     *            资源
     * @param page
     *            页码
     * @param pageSize
     *            页数
     * @return 返回满足条件的资源
     */
    @Override
    public List<Resource> selectNotExitResourcesByFunction(IRequest request, Function function, Resource resource,
            int page, int pageSize) {
        if (function == null || function.getFunctionId() == null) {
            return null;
        }
        PageHelper.startPage(page, pageSize);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("function", function);
        params.put("resource", resource);
        return functionMapper.selectNotExistsResourcesByFunction(params);
    }

    /**
     * 修改功能挂靠的resource.
     * 
     * @param request
     *            上下文请求
     * @param function
     *            功能
     * @param resources
     *            资源集合
     * @return 修改后的功能信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Function updateFunctionResources(IRequest request, Function function, List<Resource> resources) {
        if (function != null) {
            functionResourceMapper.deleteByFunctionId(function.getFunctionId());
            if (resources != null && !resources.isEmpty()) {
                for (Resource resource : resources) {
                    FunctionResource functionResource = new FunctionResource();
                    functionResource.setResourceId(resource.getResourceId());
                    functionResource.setFunctionId(function.getFunctionId());
                    functionResource.setObjectVersionNumber(1L);
                    functionResource.setCreatedBy(request.getUserId());
                    functionResource.setCreationDate(new Date());
                    functionResource.setLastUpdateDate(new Date());
                    functionResource.setLastUpdatedBy(request.getUserId());
                    functionResourceMapper.insertSelective(functionResource);
                }
            }
            roleResourceCache.reload();
        }
        return null;
    }

    public List<MenuItem> selectAllMenus(IRequest request) {
        List<Function> functions = functionCache.getGroupAll(request.getLocale());
        MenuItem root = castFunctionsToMenuItem(functions);
        return root.getChildren();
    }

    private MenuItem castFunctionsToMenuItem(List<Function> functions) {
        MenuItem root = new MenuItem();
        List<MenuItem> children = new ArrayList<>();
        root.setChildren(children);
        Map<Long, MenuItem> map = new HashMap<>();
        Iterator<Function> iterator = functions.iterator();
        while (iterator.hasNext()) {
            Function function = iterator.next();
            if (function.getParentFunctionId() == null) {
                MenuItem rootChild = createMenuItem(function);
                map.put(function.getFunctionId(), rootChild);
                children.add(rootChild);
                iterator.remove();
            }
        }

        processFunctions(map, functions);
        map.forEach((k, v) -> {
            if (v.getChildren() != null) {
                Collections.sort(v.getChildren());
            }
        });
        Collections.sort(children);
        return root;
    }

    private void processFunctions(Map<Long, MenuItem> map, List<Function> functions) {
        Iterator<Function> iterator = functions.iterator();
        while (iterator.hasNext()) {
            Function function = iterator.next();
            MenuItem parent = map.get(function.getParentFunctionId());
            if (parent != null) {
                MenuItem item = createMenuItem(function);
                map.put(function.getFunctionId(), item);
                List<MenuItem> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(item);
                iterator.remove();
            }
        }
        if (!functions.isEmpty()) {
            processFunctions(map, functions);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see IFunctionService#selectMenus(com.lkkhpg.dsis.hap.core. IRequest)
     */
    @Override
    public List<MenuItem> selectRoleFunctions(IRequest request) {
        List<Function> functions = functionCache.getGroupAll(request.getLocale());
        Long[] ids = roleFunctionService.getRoleFunctionById(request.getRoleId());

        Map<Long, Function> funcMap = new HashMap<>();
        if (functions != null) {
            for (Function f : functions) {
                funcMap.put(f.getFunctionId(), f);
            }
        }
        Map<Long, MenuItem> menuMap = new HashMap<>();
        if (ids != null) {
            for (Long fId : ids) {
                createMenuRecursive(menuMap, funcMap, fId);
            }
        }
        List<MenuItem> itemList = new ArrayList<>();
        menuMap.forEach((k, v) -> {
            if (v.getParent() == null) {
                itemList.add(v);
            }
            if (v.getChildren() != null) {
                Collections.sort(v.getChildren());
            }
        });
        Collections.sort(itemList);
        return itemList;
    }

    private MenuItem createMenuRecursive(Map<Long, MenuItem> menuMap, Map<Long, Function> funcMap, Long funcId) {
        MenuItem mi = menuMap.get(funcId);
        if (mi == null) {
            Function func = funcMap.get(funcId);
            if (func == null) {
                // role has a function that dose not exists.
                return null;
            }
            mi = createMenuItem(func);
            menuMap.put(funcId, mi);
            // create parent mi
            Long parentId = func.getParentFunctionId();
            if (parentId != null) {
                MenuItem miParent = createMenuRecursive(menuMap, funcMap, parentId);
                if (miParent != null) {
                    List<MenuItem> children = miParent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        miParent.setChildren(children);
                    }
                    mi.setParent(miParent);
                    children.add(mi);
                }
            }
        }
        return mi;
    }

    private MenuItem createMenuItem(Function function) {
        MenuItem menu = new MenuItem();
        menu.setText(function.getFunctionName());
        menu.setIcon(function.getFunctionIcon());
        menu.setFunctionCode(function.getFunctionCode());
        if (function.getResourceId() != null) {
            Resource resource = resourceService.selectResourceById(function.getResourceId());
            if (resource != null) {
                menu.setUrl(resource.getUrl());
            }
        }
        menu.setId(function.getFunctionId());
        menu.setScore(function.getFunctionSequence());
        return menu;
    }

}
