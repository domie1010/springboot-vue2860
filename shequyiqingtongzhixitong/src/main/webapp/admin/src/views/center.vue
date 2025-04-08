<template>
  <div>
    <el-form
      class="detail-form-content"
      ref="ruleForm"
      :model="ruleForm"
      label-width="80px"
    >  
     <el-row>
                                     <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='住户姓名' prop="zhuhuName">
                                  <el-input v-model="ruleForm.zhuhuName"  placeholder='住户姓名' clearable></el-input>
                              </el-form-item>
                          </el-col>

                          <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='住户手机号' prop="zhuhuPhone">
                                  <el-input v-model="ruleForm.zhuhuPhone"  placeholder='住户手机号' clearable></el-input>
                              </el-form-item>
                          </el-col>

                          <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='住户身份证号' prop="zhuhuIdNumber">
                                  <el-input v-model="ruleForm.zhuhuIdNumber"  placeholder='住户身份证号' clearable></el-input>
                              </el-form-item>
                          </el-col>

                          <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='现住地址' prop="zhuhuAddress">
                                  <el-input v-model="ruleForm.zhuhuAddress"  placeholder='现住地址' clearable></el-input>
                              </el-form-item>
                          </el-col>

                        <el-col :span="24">
                            <el-form-item v-if="flag=='zhuhu'" label='住户照片' prop="zhuhuPhoto">
                                <file-upload
                                        tip="点击上传照片"
                                        action="file/upload"
                                        :limit="3"
                                        :multiple="true"
                                        :fileUrls="ruleForm.zhuhuPhoto?ruleForm.zhuhuPhoto:''"
                                        @change="zhuhuPhotoUploadChange"
                                ></file-upload>
                            </el-form-item>
                        </el-col>
                          <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='年龄' prop="zhuhuAge">
                                  <el-input v-model="ruleForm.zhuhuAge"  placeholder='年龄' clearable></el-input>
                              </el-form-item>
                          </el-col>

                    <el-col :span="12">
                        <el-form-item v-if="flag=='zhuhu'"  label='是否注射疫苗' prop="zhuhuYimiaoTypes">
                            <el-select v-model="ruleForm.zhuhuYimiaoTypes" placeholder='请选择是否注射疫苗'>
                                <el-option
                                        v-for="(item,index) in zhuhuYimiaoTypesOptions"
                                        v-bind:key="item.codeIndex"
                                        :label="item.indexName"
                                        :value="item.codeIndex">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                          <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='来源地' prop="zhuhuLaiyuandi">
                                  <el-input v-model="ruleForm.zhuhuLaiyuandi"  placeholder='来源地' clearable></el-input>
                              </el-form-item>
                          </el-col>

                    <el-col :span="12">
                        <el-form-item v-if="flag=='zhuhu'"  label='是否有7日内核酸证明' prop="zhuhuHesuanTypes">
                            <el-select v-model="ruleForm.zhuhuHesuanTypes" placeholder='请选择是否有7日内核酸证明'>
                                <el-option
                                        v-for="(item,index) in zhuhuHesuanTypesOptions"
                                        v-bind:key="item.codeIndex"
                                        :label="item.indexName"
                                        :value="item.codeIndex">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                          <el-col :span="12">
                              <el-form-item v-if="flag=='zhuhu'"  label='行程轨迹' prop="zhuhuXingcheng">
                                  <el-input v-model="ruleForm.zhuhuXingcheng"  placeholder='行程轨迹' clearable></el-input>
                              </el-form-item>
                          </el-col>

                     <el-col :span="24">
                         <el-form-item v-if="flag=='zhuhu'"  label="备注" prop="zhuhuContent">
                             <editor
                                     style="min-width: 200px; max-width: 600px;"
                                     v-model="ruleForm.zhuhuContent"
                                     class="editor"
                                     action="file/upload">
                             </editor>
                         </el-form-item>
                     </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="flag=='zhuhu'"  label='类型' prop="zhuhuTypes">
                            <el-select v-model="ruleForm.zhuhuTypes" placeholder='请选择类型'>
                                <el-option
                                        v-for="(item,index) in zhuhuTypesOptions"
                                        v-bind:key="item.codeIndex"
                                        :label="item.indexName"
                                        :value="item.codeIndex">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
         <el-form-item v-if="flag=='users'" label="用户名" prop="username">
             <el-input v-model="ruleForm.username"
                       placeholder="用户名"></el-input>
         </el-form-item>
         <el-col :span="12">
             <el-form-item v-if="flag!='users'"  label="性别" prop="sexTypes">
                 <el-select v-model="ruleForm.sexTypes" placeholder="请选择性别">
                     <el-option
                             v-for="(item,index) in sexTypesOptions"
                             v-bind:key="item.codeIndex"
                             :label="item.indexName"
                             :value="item.codeIndex">
                     </el-option>
                 </el-select>
             </el-form-item>
         </el-col>
         <el-col :span="24">
             <el-form-item>
                 <el-button type="primary" @click="onUpdateHandler">修 改</el-button>
             </el-form-item>
         </el-col>
     </el-row>
    </el-form>
  </div>
</template>
<script>
// 数字，邮件，手机，url，身份证校验
import { isNumber,isIntNumer,isEmail,isMobile,isPhone,isURL,checkIdCard } from "@/utils/validate";

export default {
  data() {
    return {
      ruleForm: {},
      flag: '',
      usersFlag: false,
      sexTypesOptions : [],







     zhuhuYimiaoTypesOptions : [],

     zhuhuHesuanTypesOptions : [],


     zhuhuTypesOptions : [],



    };
  },
  mounted() {
    //获取当前登录用户的信息
    var table = this.$storage.get("sessionTable");
    this.sessionTable = this.$storage.get("sessionTable");
    this.role = this.$storage.get("role");
    if (this.role != "管理员"){
    }

    this.flag = table;
    this.$http({
      url: `${this.$storage.get("sessionTable")}/session`,
      method: "get"
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.ruleForm = data.data;
      } else {
        this.$message.error(data.msg);
      }
    });
      this.$http({
          url: `dictionary/page?page=1&limit=100&sort=&order=&dicCode=sex_types`,
          method: "get"
      }).then(({ data }) => {
          if (data && data.code === 0) {
          this.sexTypesOptions = data.data.list;
      } else {
          this.$message.error(data.msg);
      }
  });







   this.$http({
       url: `dictionary/page?page=1&limit=100&sort=&order=&dicCode=zhuhu_yimiao_types`,
       method: "get"
   }).then(({ data }) => {
       if (data && data.code === 0) {
          this.zhuhuYimiaoTypesOptions = data.data.list;
      } else {
          this.$message.error(data.msg);
      }
    });

   this.$http({
       url: `dictionary/page?page=1&limit=100&sort=&order=&dicCode=zhuhu_hesuan_types`,
       method: "get"
   }).then(({ data }) => {
       if (data && data.code === 0) {
          this.zhuhuHesuanTypesOptions = data.data.list;
      } else {
          this.$message.error(data.msg);
      }
    });


   this.$http({
       url: `dictionary/page?page=1&limit=100&sort=&order=&dicCode=zhuhu_types`,
       method: "get"
   }).then(({ data }) => {
       if (data && data.code === 0) {
          this.zhuhuTypesOptions = data.data.list;
      } else {
          this.$message.error(data.msg);
      }
    });



  },
  methods: {
     zhuhuPhotoUploadChange(fileUrls){
          this.ruleForm.zhuhuPhoto = fileUrls;
          this.addEditUploadStyleChange()
      },


    onUpdateHandler() {
                         if((!this.ruleForm.zhuhuName)&& 'zhuhu'==this.flag){
                             this.$message.error('住户姓名不能为空');
                             return
                         }

                             if( 'zhuhu' ==this.flag && this.ruleForm.zhuhuPhone&&(!isMobile(this.ruleForm.zhuhuPhone))){
                                 this.$message.error(`手机应输入手机格式`);
                                 return
                             }
                         if((!this.ruleForm.zhuhuIdNumber)&& 'zhuhu'==this.flag){
                             this.$message.error('住户身份证号不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuAddress)&& 'zhuhu'==this.flag){
                             this.$message.error('现住地址不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuPhoto)&& 'zhuhu'==this.flag){
                             this.$message.error('住户照片不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuAge)&& 'zhuhu'==this.flag){
                             this.$message.error('年龄不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuYimiaoTypes)&& 'zhuhu'==this.flag){
                             this.$message.error('是否注射疫苗不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuLaiyuandi)&& 'zhuhu'==this.flag){
                             this.$message.error('来源地不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuHesuanTypes)&& 'zhuhu'==this.flag){
                             this.$message.error('是否有7日内核酸证明不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuXingcheng)&& 'zhuhu'==this.flag){
                             this.$message.error('行程轨迹不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuContent)&& 'zhuhu'==this.flag){
                             this.$message.error('备注不能为空');
                             return
                         }

                         if((!this.ruleForm.zhuhuTypes)&& 'zhuhu'==this.flag){
                             this.$message.error('类型不能为空');
                             return
                         }

        if((!this.ruleForm.sexTypes)&& this.flag !='users'){
            this.$message.error('性别不能为空');
            return
        }
      if('users'==this.flag && this.ruleForm.username.trim().length<1) {
        this.$message.error(`用户名不能为空`);
        return	
      }
      this.$http({
        url: `${this.$storage.get("sessionTable")}/update`,
        method: "post",
        data: this.ruleForm
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "修改信息成功",
            type: "success",
            duration: 1500,
            onClose: () => {
            }
          });
        } else {
          this.$message.error(data.msg);
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
