import request from "../utils/request";

// 查询员工列表数据
export const queryPageApi = (name, gender,begin,end,page,pageSize) =>
  request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)


// 新增员工
export const addApi = (data) => request.post('/emps',data);

// 根据id查询员工

export const queryInfoApi = (id) => request.get(`/emps/${id}`);

// 修改员工
export const updateApi = (data) => request.put('/emps',data);

// 删除员工
export const deleteApi = (ids) => request.delete(`/emps?ids=${ids}`);