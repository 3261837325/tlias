import request from "../utils/request";

// 查询全部部门数据
export const queryAllApi = () => request.get('/depts');

// 添加部门
export const addDeptApi = (data) => request.post('/depts',data);

// 根据id查询部门
export const queryByIdApi = (id) => request.get(`/depts/${id}`);

// 修改部门
export const updateDeptApi = (data) => request.put('/depts',data);

// 删除部门
export const deleteByIdApi = (id) => request.delete(`/depts?id=${id}`);