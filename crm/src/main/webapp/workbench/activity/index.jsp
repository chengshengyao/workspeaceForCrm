<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath() + "/"; %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        //为创建 添加操作模态窗 点击按钮 绑定事件
        $(function () {
            $("#addBtn").click(function () {
                //1.找到创建 模态窗口的jQuery的对象
                //2.调用其 modal（show/hide）
                //$("#createActivityModal").modal("show")
                //从后台取得下拉列表框的值

                //日历时间组件
                $(".time").datetimepicker({
                    minView: "month",
                    language: 'zh-CN',
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    todayBtn: true,
                    pickerPosition: "bottom-left"
                });

                $.ajax({
                    url: "/workbench/activity/getUserList.do",
                    data: {},
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        var html = "<option></option>";
                        //遍历 出的N是一个user对象
                        $.each(data, function (i, n) {
                            html += "<option value='" + n.id + "'>" + n.name + "</option>";
                        })
                        $("#create-marketActivityOwner").html(html)

                        //默认填充 user.name 在option中
                        //在js【jQuery中使用el表达式 一定要套用在字符串中】
                        var id = "${user.id}";
                        $("#create-marketActivityOwner").val(id);
                        //下拉框 动态生成 后展现模态窗口
                        $("#createActivityModal").modal("show")
                    }
                })

            })
            //绑定事件执行添加操作
            $("#saveBtn").click(function () {
                $.ajax({
                    url: "/workbench/activity/save.do",
                    data: {
                        "owner": $.trim($("#create-marketActivityOwner").val()),
                        "name": $.trim($("#create-marketActivityName").val()),
                        "starDate": $.trim($("#create-startTime").val()),
                        "endDate": $.trim($("#create-endTime").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-describe").val()),
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        //市场活动添加成功，刷新市场活动列表  局部刷新
                        //pageList(1,2);
                        //$("#activityPage").bs_pagination('getOption', 'currentPage') 操作后停留当前页
                        //$("#activityPage").bs_pagination('getOption', 'rowsPerPage') 操作后维持以设置好的每页记录数
                        pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                        //清空添加模态窗口中的数据
                        //重置表单 此方法无效
                        //$("#activityAddForm").reset();
                        //应使用dom 对象中的重置方法 jQuery对象[下标]  $(dom)
                        $("#activityAddForm")[0].reset();
                        //关闭 模态窗
                        if (data.success) {
                            //关闭 模态窗
                            $("#createActivityModal").modal("hide");


                        }
                        //添加失败
                        else {
                            alert("添加市场活动失败")
                        }
                    }
                })
                //为修改按钮绑定事件，打开修改操作的模态窗
                $("#editBtn").click(function () {
                    var $xz = $("input[name=xz]:checked")
                    if ($xz.length===0){
                        alert("请选择要修改的记录")
                    }
                    else if ($xz.length>1){
                        alert("只能选中一条记录进行修改")
                    }
                    else {
                        //肯定只选了一条
                        var  id=$xz.val();
                        $.ajax({
                            url: "/workbench/activity/getUserListAndActivity.do",
                            data: {"id":id},
                            type: "get",
                            dataType: "json",
                            success: function (data) {
                                //处理 所有者下拉框
                            var html="<option></option>";
                            $.each(data.uList,function (i,n) {
                                html+="<option value='n.id'>"+n.name+"</option>";
                            })
                            $("#edit-marketActivityOwner").html(html);
                                //处理单条活动
                                $("#edit-id").val(data.a.id);
                                $("#edit-marketActivityName").val(data.a.name);
                                $("#edit-marketActivityOwner").val(data.a.owner);
                                $("#edit-startTime").val(data.a.startTime);
                                $("#edit-endTime").val(data.a.endTime);
                                $("#edit-cost").val(data.a.cost);
                                $("#edit-describe").val(data.a.description);
                                //填写信息完成 打开修改模态窗
                                $("#editActivityModal").modal("show");
                            }

                        })
                    }

                })
                //为更新按钮绑定事件,执行市场活动修改的操作
                //先做添加再做修改 的顺序
                //修改操作 可以copy 添加操作
                $("#updateBtn").click(function () {
                    $.ajax({
                        url: "/workbench/activity/update.do",
                        data: {
                            "id": $.trim($("#edit-id").val()),
                            "owner": $.trim($("#edit-marketActivityOwner").val()),
                            "name": $.trim($("#edit-marketActivityName").val()),
                            "starDate": $.trim($("#edit-startTime").val()),
                            "endDate": $.trim($("#edit-endTime").val()),
                            "cost": $.trim($("#edit-cost").val()),
                            "description": $.trim($("#edit-describe").val())
                        },
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.success) {

                               //pageList(1,2);
                                //修改操作后维持到当前页，每页展示 的记录数
                                pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                    ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                                $("#editActivityModal").modal("hide");
                            }
                            //添加失败
                            else {
                                alert("修改市场活动失败")
                            }
                        }
                    })
                })
            })
            //页面加载完毕触发  分页 pageList(页码： 1, 所展现的记录条数： 2 )
            pageList(1, 2);
            //为查询按钮绑定事件， 单击时触发pageList
            $("#searchBtn").click(function () {
                //在点击查询按钮时触发搜索，应用输入框中保存的内容进行搜索
                $("#hidden-name").val($.trim($("#search-name")))
                $("#hidden-owner").val($.trim($("#search-owner")))
                $("#hidden-startTime").val($.trim($("#search-startTime")))
                $("#hidden-endTime").val($.trim($("#search-endTime")))
                pageList(1,2);
            })
            //为全选的复选框绑定事件 触发全选操作
            $("#qx").click(function () {
                $("input[name=xz]").prop("checked",this.checked());
            })
            //动态生成的元素不能以普通绑定事件的形式进行操作
            //$("input[name=xz]").click(function () {
            //})
            // 语法 $(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)
            $("#activityBody").on("click",$("input[name=xz]"),function () {
                $("#qx").prop("checked",$("input[name=xz]").length===$("input[name=xz]:checked").length)
            })
            //为删除按钮绑定事件 执行市场活动删除的操作
            $("#deleteBtn").click(function () {
                // 找到复选框中所有 选中状态的复选框的jquery对象
                var  $xz = $("#input[name=xz]:checked");
                if ($xz.length==0){
                    alert("请选择需要删除的记录")
                }
                else {
                    if (confirm("确认删除所选中的记录吗？")){
                        //有可能选中一条记录/多条记录进行删除
                        ///workbench/activity/delete.do?id=.....
                        //拼接参数
                        var param ="";
                        //将$xz中的每一dom对象遍历出来，取其value 就相当于取得了需要删除的id
                        for ( var i=0;i<$xz;i++){
                            param += "id"+$xz[i].val()
                            //如果不是最后一个元素 需要在后面追加一个 &
                            if (i<$xz.length-1){
                                param+="&";
                            }
                        }
                        $.ajax({
                            url: "/workbench/activity/delete.do",
                            data: param,
                            type: "post",
                            dataType: "json",
                            success: function (data) {
                                if (data.success){
                                    //成功删除
                                    //pageList(1,2);
                                    //执行删除操作后维持每页所展现的记录数
                                    pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                                }
                                else {
                                    alert("市场活动删除失败")
                                }
                            }
                        })
                    }


                }
            })
        });

        //分页方法 异步请求到后台  取得最新数据 进行局部刷新展示
        //1.点此市场活动，2.添加/修改/删除 3.查询按钮市场活动列表时 4.点击分页组件查询按钮时  访问后台数据时进行调用
        //以上为pageList 指定了6个入口 在6个操作完成后进行 市场列表的刷新
        function pageList(pageNo, pageSize) {
            //将 全选的复选框的选中状态清除
              $("#qx").prop("checked",false)
            //执行查询操作前 将隐藏域中的内容取出
            $("#search-name").val($.trim($("#hidden-name")))
            $("#search-owner").val($.trim($("#hidden-owner")))
            $("#search-startTime").val($.trim($("#hidden-startTime")))
            $("#search-endTime").val($.trim($("#hidden-endTime")))
            $.ajax({
                url: "/workbench/activity/pageList.do",
                data: {
                    "pageNo":pageNo,
                    "pageSize":pageSize,
                    "name":$.trim($("#search-name").val()),
                    "owner":$.trim($("#search-owner").val()),
                    "startTime":$.trim($("#search-startTime").val()),
                    "endTime":$.trim($("#search-endTime").val())
                 },
                type: "get",
                dataType: "json",
                success: function (data) {
                var html= "";
                //n 就是遍历出的市场活动对象
                $.each(data.dadalist,function (i,n) {
                                  html+='<tr class="active">';
                                  html+='<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
                                  html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                                  html+='<td>'+n.owner+'</td>';
                                  html+='<td>'+n.startTime+'</td>';
                                  html+='<td>'+n.endTime+'</td>';
                                  html+='</tr>';
                })
                    $("#activityBody").html(html);
                    //    数据处理完毕后结合 分页查询对前端展现分页的信息
                    //计算总页数
                    var totalPages =data.total%pageSize===0?data.total/pageSize:parseInt(data.total/pageSize)+1;
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 3, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        //回调函数在 点击分页插件时触发
                        onChangePage : function(event, data){
                            pageList(data.currentPage , data.rowsPerPage);
                        }
                    });
                }
            })
        }
    </script>
</head>
<body>
<%--隐藏域保存搜索框中的内容--%>
<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startTime">
<input type="hidden" id="hidden-endTime">
<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form" id="activityAddForm">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <%--拼接下拉单选框--%>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startTime" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endTime" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <%--data-dismiss 【关闭模态窗】--%>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">

                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startTime">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endTime" >
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="search-startTime"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="search-endTime">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <%--
                data-toggle="modal  触发按钮打开的模块窗操作
                data-target="#createActivityModal"  打开 所创建市场活动模态窗  #绑定id
                通过属性与属性值得方式 使用按钮 触发 模态窗  无法对按钮功能进行扩充
                 应该由 js实现模态窗
                --%>
                <button type="button" class="btn btn-primary" id="addBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
<%--                <tr class="active">--%>
<%--                    <td><input type="checkbox"/></td>--%>
<%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
<%--                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                    <td>zhangsan</td>--%>
<%--                    <td>2020-10-10</td>--%>
<%--                    <td>2020-10-20</td>--%>
<%--                </tr>--%>
<%--                <tr class="active">--%>
<%--                    <td><input type="checkbox"/></td>--%>
<%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
<%--                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                    <td>zhangsan</td>--%>
<%--                    <td>2020-10-10</td>--%>
<%--                    <td>2020-10-20</td>--%>
<%--                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage"></div>


        </div>

    </div>

</div>
</body>
</html>