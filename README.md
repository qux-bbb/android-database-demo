# android-database-demo
android database demo

看了看android的数据库操作  

在开启之后直接调用 queryInfo  
主界面只有一个 Add 按钮，点击之后跳到AddActivity填写信息之后返回MainActivity执行 addInfo  
长按列表中一项，会弹窗询问操作，选择Update即跳到 UpdateActivity 填写信息返回MainActivity执行 updateInfo  
如果选择Delete，则调用deleteInfo删除该项  

在addInfo，deleteInfo,updateInfo中，最后都会调用 queryInfo，以更新界面  

大概是这样了
