<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/main' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item>Goods</el-breadcrumb-item>
    </el-breadcrumb>
    <br />
    <br />

    <span>
      Search：
      <el-input
        style="width:200px"
        placeholder="Goods title"
        @input="search"
        v-model="search_goodstitle"
      ></el-input>
    </span>
    <el-button type="danger" icon="el-icon-search"></el-button>
    <el-button type="danger" plain icon="el-icon-plus" @click="add">Add</el-button>
    <el-divider></el-divider>
    <el-table
      :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)"
      style="width: 100%"
      class="table-check"
      @selection-change="handleSelectionChange"
      height="550"
    >
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="title" label="Goods title"></el-table-column>
      <el-table-column
        prop="key_words"
        label="Goods type"
        :filter-method="filterHandler"
        :filters="[{ text: 'Phone', value: 'Phone' },{ text: 'Computer', value: 'Computer' }, { text: 'Instrument', value: 'Instrument'}, { text: 'Appliances', value: 'Appliances'}]"
      ></el-table-column>
      <el-table-column prop="remark" label="Goods picture">
        <template slot-scope="scope">
          <img :src="scope.row.remark" width="100" />
        </template>
      </el-table-column>
      <el-table-column prop="retail_price" label="Goods price"></el-table-column>
      <el-table-column prop="replenishment_period" label="Stock"></el-table-column>
      <el-table-column
        prop="sts_cd"
        label="State"
        :filter-method="filterHandler"
        :filters="[{ text: 'Not in warehouse', value: 'Not in warehouse' },{ text: 'In warehouse', value: 'In warehouse' }, { text: 'On shelf', value: 'On shelf'}]"
      >
        <template slot-scope="scope">
          <el-tag :type="tag(scope.row.sts_cd)" disable-transitions>{{scope.row.sts_cd}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="operation" width="250">
        <template slot-scope="scope">
          <el-button type="success" size="small" icon="el-icon-edit" @click="edit(scope.row)"></el-button>
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="remove(scope.row, scope.$index)"
          ></el-button>
          <el-button
            :type="btntag(scope.row.sts_cd)"
            size="small"
            @click="operate(scope.row)"
          >{{btn(scope.row.sts_cd)}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br>
     <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5,10,tableData.length]"
      :page-size="pagesize"
      layout="total,sizes,prev,pager,next,jumper"
      :total="tableData.length"
    ></el-pagination>
    <br />
    <span>Batch：</span>
    <el-button @click="batch(msg[0])" type="danger">Push</el-button>
    <el-button @click="batch(msg[1])" type="success">Shelve</el-button>
    <el-button @click="batch(msg[2])" type="warning">Unshelve</el-button>
    <el-drawer title="drawer" :visible.sync="drawer" size="50%" :with-header="false" class="slip">
      <div class="form-div">
        <h3>Goods information</h3>
        <el-form
          :model="addGoodsForm"
          ref="addGoodsForm"
          label-width="150px"
          class="add-goods-form"
        >
          <el-form-item
            label="Goods title"
            prop="title"
            :rules="[{ required: true, message: 'Please enter the goods title'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.title" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item label="Goods brand" prop="brand_options">
            <!-- :rules="[{ required: true, message: 'Please choose the Goods brand', trigger: 'blur'}]" -->

            <el-select
              style="width:35rem"
              placeholder
              v-model="addGoodsForm.brd_id"
              autocomplete="off"
            >
              <el-option
                v-for="item in brand_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <br />
          <el-form-item
            label="SKU Code"
            prop="sku_cd"
            :rules="[{ required: true, message: 'Please enter the sku_cd code'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.sku_cd" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="UPC Code"
            prop="upc"
            :rules="[{ required: true, message: 'Please enter the upc code'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.upc" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="ean Code"
            prop="ean"
            :rules="[{ required: true, message: 'Please enter the ean code'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.ean" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Goods type"
            prop="key_words"
            :rules="[{ required: true, message: 'Please choose the Goods type', trigger: 'blur'}]"
          >
            <el-select
              style="width:35rem"
              placeholder
              v-model="addGoodsForm.key_words"
              autocomplete="off"
            >
              <el-option
                v-for="item in type_options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <br />
          <el-form-item
            label="Goods model"
            prop="model"
            :rules="[{ required: true, message: 'Please enter the Goods model'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.model" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Goods price"
            prop="retail_price"
            :rules="[{ required: true, message: 'Please enter the Goods price'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.retail_price" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Goods stocks"
            prop="replenishment_period"
            :rules="[{ required: true, message: 'Please enter the Goods stocks'}]"
          >
            <el-input
              style="width:35rem"
              v-model="addGoodsForm.replenishment_period"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <br />
          <el-form-item
            label="Warranty period"
            prop="warranty_day"
            :rules="[{ required: true, message: 'Please enter the Warranty period'}]"
          >
            <el-input style="width:35rem" v-model="addGoodsForm.warranty_day" autocomplete="off"></el-input>
          </el-form-item>
          <br />
          <div class="flex">
            <div>
              <span>Goods picture:</span>
              <br />
              <br />
              <el-upload
                style="width:24rem"
                ref="upload"
                drag
                action="http://39.99.161.110:8088/product/uploadPicture"
                :name="fileName"
                :file-list="fileList"
                :on-success="handleSuccess"
                :on-change="fileChange"
                :before-upload="uploadPicture"
                :auto-upload="false"
                list-type="picture"
                :limit="1"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  Drag the file here, or
                  <em>Click to upload</em>
                </div>
                <div
                  class="el-upload__tip"
                  slot="tip"
                >It is recommended to upload JPG/PNG files and not exceed 500kb</div>
              </el-upload>
            </div>
            <div>
              <span>Goods parameters:</span>
              <br />
              <br />
              <el-form-item
                label="Length(cm)"
                prop="length"
                :rules="[{ required: true, message: 'Please enter the length'}]"
              >
                <el-input style="width:12rem" v-model="addGoodsForm.length" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item
                label="Width(cm)"
                prop="width"
                :rules="[{ required: true, message: 'Please enter the width'}]"
              >
                <el-input style="width:12rem" v-model="addGoodsForm.width" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item
                label="Height(cm)"
                prop="height"
                :rules="[{ required: true, message: 'Please enter the height'}]"
              >
                <el-input style="width:12rem" v-model="addGoodsForm.height" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item
                label="Weight(kg)"
                prop="weight"
                :rules="[{ required: true, message: 'Please enter the weight'}]"
              >
                <el-input style="width:12rem" v-model="addGoodsForm.weight" autocomplete="off"></el-input>
              </el-form-item>
            </div>
          </div>
          <br />
          <span>eBay description：</span>
          <mavon-editor
            :subfield="false"
            :toolbars="toolbars"
            class="markdown"
            v-model="addGoodsForm.ebay_description"
            ref="md"
          />
          <br />
          <span>Amazon description：</span>
          <mavon-editor
            :subfield="false"
            :toolbars="toolbars"
            class="markdown"
            v-model="addGoodsForm.amazon_description"
            ref="md"
          />

          <el-form-item class="goods-form-button">
            <el-button type="danger" @click="submitForm('addGoodsForm')">Save</el-button>
            <el-button @click="darCancel('addGoodsForm')">Cancel</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        mark: true, // 标记
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: false, // code
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      },
      count: 0,
      fileList: [],
      fileName: "fileName",
      isAdd: false,
      currentPage: 1, //默认页码为1
      pagesize: 5, //默认一页显示11条
      // brand_options: [{ value: 1, label: "p" }],
      brand_options: [],
      type_options: [
        { value: "Phone", label: "Phone" },
        { value: "Computer", label: "Computer" },
        { value: "Instrument", label: "Instrument" },
        { value: "Appliances", label: "Appliances" }
      ],
      multipleSelection: [],
      msg: ["Not in warehouse", "In warehouse", "On shelf"],
      addGoodsForm: {
        pro_id: "",
        title: "",
        name_en: "",
        retail_price: "",
        replenishment_period: "",
        remark: "",
        sku_cd: "",
        key_words: "",
        upc: "",
        warranty_day: "",
        ean: "",
        model: "",
        length: "",
        width: "",
        height: "",
        weight: "",
        ebay_description: "",
        amazon_description: "",
        brd_id: ""
      },
      ori_form: {
        pro_id: "",
        title: "",
        name_en: "",
        retail_price: "",
        replenishment_period: "",
        remark: "",
        sku_cd: "",
        key_words: "",
        upc: "",
        warranty_day: "",
        ean: "",
        model: "",
        length: "",
        width: "",
        height: "",
        weight: "",
        ebay_description: "",
        amazon_description: "",
        brd_id: ""
      },
      search_goodstitle: "",
      tableData: [],
      table: [],
      opRow: "",
      drawer: false
    };
  },
  mounted() {
    this.$post("brd/getBrand", {
      user_id: sessionStorage.getItem("user_id")
    }).then(res => {
      let tmpBrands = [];
      for (let i = 0; i < res.data.length; i++) {
        tmpBrands[i] = {
          value: res.data[i].brd_id,
          label: res.data[i].name_en
        };
      }
      this.brand_options = tmpBrands;
    }),
      this.$post("/product/getProducts", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          return;
        }
        if (res.code == 200) {
          this.tableData = res.data;
          this.table = res.data;
        }
      });
  },
  methods: {
    handleSizeChange(size) {
      //一页显示多少条
      this.pagesize = size;
    },
    handleCurrentChange(currentPage) {
      //页码更改方法
      this.currentPage = currentPage;
    },
    batch(msg) {
      if (this.multipleSelection == 0) {
        this.$message.warning("Please select item");
        return;
      }
      if (
        this.multipleSelection.filter(e => e.sts_cd == msg).length !=
        this.multipleSelection.length
      ) {
        this.$message.warning(`Please select items that ${msg}`);
        return;
      }
      let nextStage = "";
      if (msg == "Not in warehouse") {
        nextStage = "In warehouse";
      }
      if (msg == "In warehouse") {
        nextStage = "On shelf";
      }
      if (msg == "On shelf") {
        nextStage = "In warehouse";
      }
      this.multipleSelection.forEach(element => {
        this.$post("/product/updateSts", {
          pro_id: element.pro_id,
          sts_cd: nextStage
        }).then(res => {
          if (res.code == 504) {
            this.$message.warning(res.message);
            return;
          }
          if (res.code == 200) {
            this.refresh();
          }
        });
      });
      this.$message.success("Success");
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    filterHandler(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    add() {
      if(sessionStorage.getItem("user_id") == 3){
        this.$message.warning("Admin can not operate")
        return
      }
      console.log(this.addGoodsForm);
      this.addGoodsForm.brd_id = "";
      // this.addGoodsForm
      this.isAdd = true;
      this.drawer = true;
    },
    remove(row, index) {
      this.$post("/product/deleteProduct", {
        pro_id: row.pro_id
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          this.refresh();
          return;
        }
        if (res.code == 200) {
          this.$message.success(res.message);
          this.refresh();
        }
      });
    },
    darCancel(formName) {
      this.addGoodsForm.ebay_description = "";
      this.addGoodsForm.amazon_description = "";
      this.$refs[formName].resetFields();
      this.drawer = false;
      this.count = 0;
      this.fileList = [];
    },
    fileChange() {
      this.count++;
      console.log("count");
      console.log(this.count);
    },
    uploadPicture(file) {
    },
    handleSuccess(res) {
      console.log("handlesuccess");
      if (this.isAdd) {
        console.log("add");
        // console.log(this.addGoodsForm.brd_id);
        this.isAdd = false;
        this.$post("/product/addProduct", {
          brd_id: this.addGoodsForm.brd_id,
          retail_price: this.addGoodsForm.retail_price,
          sku_cd: this.addGoodsForm.sku_cd,
          title: this.addGoodsForm.title,
          upc: this.addGoodsForm.upc,
          ean: this.addGoodsForm.ean,
          name_en: this.addGoodsForm.name_en,
          ebay_description: this.addGoodsForm.ebay_description,
          amazon_description: this.addGoodsForm.amazon_description,
          key_words: this.addGoodsForm.key_words,
          width: this.addGoodsForm.width,
          height: this.addGoodsForm.height,
          weight: this.addGoodsForm.weight,
          length: this.addGoodsForm.length,
          model: this.addGoodsForm.model,
          replenishment_period: this.addGoodsForm.replenishment_period,
          warranty_day: this.addGoodsForm.warranty_day,
          remark: res.data,
          user_id: sessionStorage.getItem("user_id")
        }).then(res => {
          if (res.code == 504) {
            this.$message.warning(res.message);
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
          if (res.code == 200) {
            this.$message.success(res.message);
            this.refresh();
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
        });
        this.drawer = false;
        return;
      }
      if (!this.isAdd) {
        console.log("update");
        // console.log(this.addGoodsForm.title);
        console.log(this.addGoodsForm);
        // let title = this.addGoodsForm.title;
        // console.log(title)
        this.$post("/product/updateProduct", {
          brd_id: this.addGoodsForm.brd_id,
          retail_price: this.addGoodsForm.retail_price,
          sku_cd: this.addGoodsForm.sku_cd,
          title: this.addGoodsForm.title,
          sts_cd: this.addGoodsForm.sts_cd,
          created_by: this.addGoodsForm.created_by,
          creation_date: this.addGoodsForm.creation_date,
          upc: this.addGoodsForm.upc,
          ean: this.addGoodsForm.ean,
          name_en: this.addGoodsForm.name_en,
          ebay_description: this.addGoodsForm.ebay_description,
          amazon_description: this.addGoodsForm.amazon_description,
          key_words: this.addGoodsForm.key_words,
          width: this.addGoodsForm.width,
          height: this.addGoodsForm.height,
          weight: this.addGoodsForm.weight,
          length: this.addGoodsForm.length,
          model: this.addGoodsForm.model,
          replenishment_period: this.addGoodsForm.replenishment_period,
          warranty_day: this.addGoodsForm.warranty_day,
          remark: res.data,
          user_id: sessionStorage.getItem("user_id"),
          pro_id: this.addGoodsForm.pro_id
        }).then(res => {
          if (res.code == 504) {
            this.$message.warning(res.message);
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
          if (res.code == 200) {
            this.$message.success(res.message);
            this.refresh();
            this.$refs["addGoodsForm"].resetFields();
            this.addGoodsForm.ebay_description = "";
            this.addGoodsForm.amazon_description = "";
            this.count = 0;
            this.fileList = [];
            return;
          }
        });
        this.drawer = false;
        return;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // if (this.count == 0) {
          //   this.$message.warning("Please upload goods picture");
          //   return;
          // }
          if (
            this.addGoodsForm.ebay_description == "" ||
            this.addGoodsForm.amazon_description == ""
          ) {
            this.$message.warning("Please enter the description");
            return;
          }

          if (!this.isAdd && this.count == 0) {
            console.log("update");
            // console.log(this.addGoodsForm.title);
            console.log(this.addGoodsForm);
            // let title = this.addGoodsForm.title;
            // console.log(title)
            this.$post("/product/updateProduct", {
              brd_id: this.addGoodsForm.brd_id,
              retail_price: this.addGoodsForm.retail_price,
              sku_cd: this.addGoodsForm.sku_cd,
              title: this.addGoodsForm.title,
              sts_cd: this.addGoodsForm.sts_cd,
              created_by: this.addGoodsForm.created_by,
              creation_date: this.addGoodsForm.creation_date,
              upc: this.addGoodsForm.upc,
              ean: this.addGoodsForm.ean,
              name_en: this.addGoodsForm.name_en,
              ebay_description: this.addGoodsForm.ebay_description,
              amazon_description: this.addGoodsForm.amazon_description,
              key_words: this.addGoodsForm.key_words,
              width: this.addGoodsForm.width,
              height: this.addGoodsForm.height,
              weight: this.addGoodsForm.weight,
              length: this.addGoodsForm.length,
              model: this.addGoodsForm.model,
              replenishment_period: this.addGoodsForm.replenishment_period,
              warranty_day: this.addGoodsForm.warranty_day,
              remark: this.addGoodsForm.remark,
              user_id: sessionStorage.getItem("user_id"),
              pro_id: this.addGoodsForm.pro_id
            }).then(res => {
              if (res.code == 504) {
                this.$message.warning(res.message);
                this.$refs["addGoodsForm"].resetFields();
                this.addGoodsForm.ebay_description = "";
                this.addGoodsForm.amazon_description = "";
                this.count = 0;
                this.fileList = [];
                return;
              }
              if (res.code == 200) {
                this.$message.success(res.message);
                this.refresh();
                this.$refs["addGoodsForm"].resetFields();
                this.addGoodsForm.ebay_description = "";
                this.addGoodsForm.amazon_description = "";
                this.count = 0;
                this.fileList = [];
                return;
              }
            });
            this.drawer = false;
            return;
          }

          if (this.count == 0 && this.isAdd) {
            this.$message.warning("Please upload goods picture");
            return;
          }
          this.$refs.upload.submit();
        } else {
          return false;
        }
      });
    },
    check() {
      console.log(this.addGoodsForm.title);
      console.log(this.addGoodsForm);
    },
    edit(row) {
      this.isAdd = false;
      this.drawer = true;
      this.$nextTick(function() {
        this.addGoodsForm = JSON.parse(JSON.stringify(row));
        // this.addGoodsForm = row;
      });

    },
    btntag(msg) {
      if (msg == "Not in warehouse") return "danger";
      if (msg == "In warehouse") return "success";
      if (msg == "On shelf") return "warning";
    },
    btn(msg) {
      if (msg == "Not in warehouse") return "Push";
      if (msg == "In warehouse") return "Shelve";
      if (msg == "On shelf") return "Unshelve";
    },
    tag(msg) {
      if (msg == "Not in warehouse") return "danger";
      if (msg == "In warehouse") return "warning";
      if (msg == "On shelf") return "success";
    },
    search() {
      this.tableData = this.table.filter(e =>
        e.title.match(this.search_goodstitle)
      );
    },
    refresh() {
      this.$post("/product/getProducts", {
        user_id: sessionStorage.getItem("user_id")
      }).then(res => {
        if (res.code == 504) {
          this.$message.warning(res.message);
          this.tableData = res.data;
          return;
        }
        if (res.code == 200) {
          this.tableData = res.data;
        }
      });
    },
    operate(row) {
      let nextStage = "";
      if (row.sts_cd == "Not in warehouse") {
        nextStage = "In warehouse";
      }
      if (row.sts_cd == "In warehouse") {
        nextStage = "On shelf";
      }
      if (row.sts_cd == "On shelf") {
        nextStage = "In warehouse";
      }
      this.$post("/product/updateSts", {
        pro_id: row.pro_id,
        sts_cd: nextStage
      }).then(res => {
        // console.log(res)
        if (res.code == 504) {
          this.$message.warning(res.message);
          return;
        }
        if (res.code == 200) {
          this.refresh();
        }
      });
    }
  }
};
</script>

<style scoped>
.goods-form-button {
  margin-top: 2rem;
  margin-left: 30rem;
}
.markdown {
  margin-top: 1rem;
  height: 25rem;
  width: 50rem;
}
.flex {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.slip >>> .el-drawer__body {
  overflow-y: auto;
}
.form-div {
  margin-left: 3rem;
}
.add-goods-form {
  margin-top: 2rem;
}
.top {
  display: flex;
  flex-direction: row;
  width: 20%;
  font-size: 15px;
  font-weight: bold;
  justify-content: space-between;
}
.table-check >>> .el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #f56c6c;
  border-color: #f56c6c;
}
</style>