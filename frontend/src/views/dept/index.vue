<script setup>
import { ref, onMounted } from 'vue'
import {queryAllApi, addDeptApi, queryByIdApi,updateDeptApi,deleteByIdApi} from '@/api/dept'
import { ElMessage,ElMessageBox } from 'element-plus'



const dialogFormVisible = ref(false) // 控制对话框的显示与隐藏
const deptFrom = ref({name: ''}) // 部门表单填入的数据
const formTitle = ref('') // 对话框的标题

const deptFormRef = ref() // 部门表单
// 表单的验证规则
const rules = ref({
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ]
})
// 添加部门
const add = () => { 
  dialogFormVisible.value = true
  formTitle.value = '添加部门'
  deptFrom.value = {name: ''}
  //重置表单的校验规则-提示信息
  if (deptFormRef.value){
    deptFormRef.value.resetFields();
  }
}


// 保存部门
const save = async () => {
  //表单校验
  if(!deptFormRef.value) return;
  deptFormRef.value.validate(async (valid) => { //valid 表示是否校验通过: true 通过 / false  不通过
    if(valid){ //通过

      let result;
      if(deptFrom.value.id){ //编辑
        result = await updateDeptApi(deptFrom.value);
      }else{ 
        result = await addDeptApi(deptFrom.value);
      }

    
      if(result.code){ //成功
        //提示信息
        ElMessage.success('操作成功');
        //关闭对话框
        dialogFormVisible.value = false;
        //查询
        search();
      }else{ //失败
        ElMessage.error(result.msg);
      }
    }else { //不通过
      ElMessage.error('表单校验不通过');
    }
  })
}

// 编辑部门
const edit = async (id) => { 
  formTitle.value = '编辑部门'
  if (deptFormRef.value){
    deptFormRef.value.resetFields();
  }
  const result = await queryByIdApi(id);
  if(result.code === 1) {
    dialogFormVisible.value = true
    deptFrom.value = result.data
  }
  

}

// 删除部门
const deleteById = async (id) => { 
 console.log(`Delete item with ID ${id}`);
  //删除部门时, 需要弹出一个确认框, 如果是确认, 则删除部门
  ElMessageBox.confirm('此操作将永久删除该部门, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 删除部门
    const result = await deleteByIdApi(id)
    if(result.code){
      ElMessage.success('删除成功')
      search()
    }else{ //失败
      ElMessage.error(result.msg)
    }
  })
};
const search = async () => { 
  
  const result = await queryAllApi();
  if (result.code === 1) {
    deptList.value = result.data
  }
}
onMounted(() => {
  search()
})
const deptList = ref([
])

</script>

<template>
  <h1>部门管理</h1>
  <el-button type="primary" @click="add()">+ 添加部门</el-button>
  <!-- 部门表格 -->
   <el-table :data="deptList" border style="width: 100%">
    <el-table-column type="index" label="序号" width="100" />
    <el-table-column prop="name" label="部门名称" width="180" />
    <el-table-column prop="updateTime" label="修改日期" width="180" />
    <el-table-column prop="address" label="操作" width="180" >
       <template #default="scope">
  <el-button type="primary" @click = "edit(scope.row.id)">编辑</el-button>
  <el-button type="danger" @click = "deleteById(scope.row.id)">删除</el-button>
        
      </template>
    </el-table-column>
  </el-table>
  <!-- 新增部门表单 -->
    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="deptFrom" :rules="rules" ref="deptFormRef">
      <el-form-item label="输入部门名称" :label-width="120" prop="name">
        <el-input v-model="deptFrom.name"  />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>

</style>
