<%@ page import="java.util.List" %>
<%@ page import="com.bjpowernode.crm.settings.domain.DicValue" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.bjpowernode.crm.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath() + "/";
    List<DicValue> dvList = (List<DicValue>) application.getAttribute("stageList");
    Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");
    Set<String> set = pMap.keySet();
    int point = 0;
    for (int i = 0; i < dvList.size(); i++) {
        DicValue dv = dvList.get(i);
        String stage = dv.getValue();
        String possibility = pMap.get(stage);
        if ("0".equals(possibility)) {
            point = i;
            break;
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>

    <style type="text/css">
        .mystage {
            font-size: 20px;
            vertical-align: middle;
            cursor: pointer;
        }

        .closingDate {
            font-size: 15px;
            cursor: pointer;
            vertical-align: middle;
        }
    </style>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $(".remarkDiv").mouseover(function () {
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function () {
                $(this).children("span").css("color", "red");
            });

            $(".myHref").mouseout(function () {
                $(this).children("span").css("color", "#E6E6E6");
            });


            //阶段提示框
            $(".mystage").popover({
                trigger: 'manual',
                placement: 'bottom',
                html: 'true',
                animation: false
            }).on("mouseenter", function () {
                var _this = this;
                $(this).popover("show");
                $(this).siblings(".popover").on("mouseleave", function () {
                    $(_this).popover('hide');
                });
            }).on("mouseleave", function () {
                var _this = this;
                setTimeout(function () {
                    if (!$(".popover:hover").length) {
                        $(_this).popover("hide")
                    }
                }, 100);
            });
            //在页面加载完毕后，展现交易历史列表
            showHistoryList();
        });

        function showHistoryList() {
            $.ajax({
                url: "/workbench/activity/getHistoryListByTranId.do",
                data: {
                    "tranId": "${t.id}",
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    var html = "";
                    $.each(data, function (i, n) {
                        html += '<tr>'
                        html += '<td>' + n.stage + '</td>'
                        html += '<td>' + n.money + '</td>'
                        html += '<td>' + n.possibility + '</td>'
                        html += '<td>' + n.expectedDate + '</td>'
                        html += '<td>' + n.createTime + '</td>'
                        html += '<td>' + n.createBy + '</td>'
                        html += '</tr>'
                    })
                    $("#tranHistoryBody").html(html)
                }
            })
        }

        //改变交易阶段 需要改变的阶段 需要改变阶段的下标
        function changeStage(stage, i) {
            $.ajax({
                url: "/workbench/activity/changeStage.do",
                data: {
                    "id": "${t.id}",
                    "stage": stage,
                    "money": "${t.money}",
                    "expectedDate": "${t.expectedDate}"
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        $("#stage").html(data.t.stage)
                        $("#possibility").html(data.t.possibility)
                        $("#editTime").html(data.t.editTime)
                        $("#editBy").html(data.t.editBy)

                        //换图标
                        changeicon(stage, i)
                    } else {
                        alert("改变阶段失败")
                    }
                }
            })
        }

        function changeicon(stage, index1) {
            var currentStage = stage;
            var currentPossibility = $("#possibility").html();
            //当前阶段下标
            var index = index1;
            //分解点
            var point = "<%=point%>"
            //当前阶段的可能性为0 前7个 为黑圈 后两个 为叉
            if ("0" === currentPossibility) {
                for (var i = 0; i < point; i++) {
                    //遍历前七个
                    //--------黑圈
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-record mystage");
                    $("#" + i).css("color", "#000000")
                }
                for (var i = point; i <<%=dvList.size()%>; i++) {
                    //遍历后两个
                    //----红叉或黑叉
                    if (i === index) {
                        //当前阶段 ------红叉
                        $("#" + i).removeClass();
                        $("#" + i).addClass("glyphicon glyphicon-remove  mystage");
                        $("#" + i).css("color", "#ff0000")
                    } else {
                        //-----黑叉
                        $("#" + i).removeClass();
                        $("#" + i).addClass("glyphicon glyphicon-remove  mystage");
                        $("#" + i).css("color", "#000000")
                    }
                }
            }
            //当前阶段的可能性不为0 前七个 包含 绿圈 绿色标记 黑圈 后两个 一定是黑叉
            else {
                for (var i = 0; i < point; i++) {
                    //遍历前七个
                    if (i == index) {
                        //当前阶段 绿色标记
                        $("#" + i).removeClass();
                        $("#" + i).addClass("glyphiconglyphicon-map-marker mystage");
                        $("#" + i).css("color", "#90F790")
                    } else if (i < index) {
                        //是 绿圈
                        $("#" + i).removeClass();
                        $("#" + i).addClass("glyphiconglyphicon-ok-circle mystage");
                        $("#" + i).css("color", "#90F790")
                    } else if (i > index) {
                        //黑圈
                        $("#" + i).removeClass();
                        $("#" + i).addClass("glyphicon glyphicon-record mystage");
                        $("#" + i).css("color", "#000000")
                    }
                }
                for (var i = point; i <<%=dvList.size()%>; i++) {
                    //遍历后两个 -----黑叉
                    $("#" + i).removeClass();
                    $("#" + i).addClass("glyphicon glyphicon-remove  mystage");
                    $("#" + i).css("color", "#000000")

                }

            }
        }
    </script>

</head>
<body>

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>${t.customerId}-${t.name} <small>￥${t.money}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" onclick="window.location.href='edit.jsp';"><span
                class="glyphicon glyphicon-edit"></span> 编辑
        </button>
        <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>

<!-- 阶段状态 -->
<div style="position: relative; left: 40px; top: -50px;">
    阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <%
        String currentStage = ((Tran) request.getAttribute("t")).getStage();
        String currentPossibility = pMap.get(currentStage);
        //寻找阶段 下标！！！！！！
        // 根据 当前阶段可能性是否为0？？？？？？？？？？？？？？？？？？？？？？？

        // 【主：当前可能性为0】  2种情况 ---- 1.红叉  2.黑叉 说明进行至第八状态 或第九阶段 失败
        if ("0".equals(currentPossibility)) {
            for (int i = 0; i < dvList.size(); i++) {
                DicValue dv = dvList.get(i);
                String stage = dv.getValue();
                String possibility = pMap.get(stage);
                //遍历出整个 交易过程中 可能性 	1.为0  2.不为0
                if ("0".equals(possibility)) {
                    // 【1.为0】   区分 以哪种方式失败交易
                    if (possibility.equals(currentPossibility)) {
                        // 【1.1】红叉 以该形式【当前阶段】的交易方式 交易失败

    %>
    <span class="glyphicon glyphicon-remove mystage" style="color: red;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------
    <%
    } else {
        // 【1.2】黑叉 表示整个过程交易失败

    %>
    <span class="glyphicon glyphicon-record mystage" style="color: #000000;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------

    <%

        }
    }
    //交易失败状态下 前7个阶段 可能性应该不为0 的状态
    else {
        //----------黑圈-------------
    %>
    <span class="glyphicon glyphicon-record mystage" style="color: #000000;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------

    <%
            }
        }
    }
    //当前阶段可能性不为0 前七个阶段进行区分
    else {
        //当前阶段可能性不为零 有3^7种情况 完成了【绿圈】 进行中【绿色标记】 未完成【黑圈】
        //准备当前阶段的下标
        int index = 0;
        for (int i = 0; i < dvList.size(); i++) {
            DicValue dv = dvList.get(i);
            String stage = dv.getValue();
            //String possibility = pMap.get(stage);
            //若遍历出当前阶段【下标0-6】
            if (currentStage.equals(stage)) {
                index = i;
                break;
            }

        }
        //非当前阶段可能性不为0
        for (int i = 0; i < dvList.size(); i++) {
            DicValue dv = dvList.get(i);
            String stage = dv.getValue();
            String possibility = pMap.get(stage);
            //遍历出  阶段可能性为0 为后两个阶段
            if ("0".equals(possibility)) {
                //黑叉------------------------
    %>
    <span class="glyphicon glyphicon-record mystage" style="color: #000000;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------

    <%
    } else {
        //遍历出阶段 且可能性也不为0 前七个阶段 绿圈 绿色标记 黑圈
        if (i == index) {
            //是当前阶段
            //绿色标记------------------------------
    %>
    <span class="glyphicon glyphicon-map-marker mystage" style="color: #90F790;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------

    <%

    } else if (i < index) {
        //绿圈------------------------------
    %>
    <span class="glyphicon glyphicon-ok-circle mystage" style="color: #90F790;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------

    <%
    } else if (i > index) {
        //黑圈------------------------------
    %>
    <span class="glyphicon glyphicon-record mystage" style="color: #000000;"
          id="<%=i%>"
          onclick="changeStage('<%=stage%>','<%=i%>')"
          data-toggle="popover"
          data-placement="bottom"
          data-content="<%=dv.getText()%>"
    >
    </span>
    -----------

    <%
                    }
                }
            }
        }

    %>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="资质审查" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>--%>
    <%--		-------------%>
    <span class="closingDate">${t.expectedDate}</span>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: 0px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.owner}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.money}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.customerId}-${t.name}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.expectedDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">客户名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.customerId}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${t.stage}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">类型</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.type}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="possibility">${t.possibility}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">来源</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.source}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.activityId}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">联系人名称</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.contactsId}</b></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${t.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 70px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b
                id="editBy">${t.editBy}&nbsp;&nbsp;${t.editTime}</b><small
                id="editTime" style="font-size: 10px; color: gray;">${t.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 80px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${t.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 90px;">
        <div style="width: 300px; color: gray;">联系纪要</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                &nbsp;${t.contaSummary}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 100px;">
        <div style="width: 300px; color: gray;">下次联系时间</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.nextContactTime}&nbsp;</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 100px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 备注1 -->
    <div class="remarkDiv" style="height: 60px;">
        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;">
            <h5>哎呦！</h5>
            <font color="gray">交易</font> <font color="gray">-</font> <b>${t.customerId}-${t.customerId}</b> <small
                style="color: gray;"> ${t.customerId} 由 ${t.customerId}</small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>

    <!-- 备注2 -->
    <div class="remarkDiv" style="height: 60px;">
        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;">
            <h5>呵呵！</h5>
            <font color="gray">交易</font> <font color="gray">-</font> <b>${t.customerId}-${t.customerId}</b> <small
                style="color: gray;"> ${t.customerId} ${t.customerId}</small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary">保存</button>
            </p>
        </form>
    </div>
</div>

<!-- 阶段历史 -->
<div>
    <div style="position: relative; top: 100px; left: 40px;">
        <div class="page-header">
            <h4>阶段历史</h4>
        </div>
        <div style="position: relative;top: 0px;">
            <table id="activityTable" class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>阶段</td>
                    <td>金额</td>
                    <td>可能性</td>
                    <td>预计成交日期</td>
                    <td>创建时间</td>
                    <td>创建人</td>
                </tr>
                </thead>
                <tbody id="tranHistoryBody">
                <%--						<tr>--%>
                <%--							<td>资质审查</td>--%>
                <%--							<td>5,000</td>--%>
                <%--							<td>10</td>--%>
                <%--							<td>2017-02-07</td>--%>
                <%--							<td>2016-10-10 10:10:10</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--						</tr>--%>
                <%--						<tr>--%>
                <%--							<td>需求分析</td>--%>
                <%--							<td>5,000</td>--%>
                <%--							<td>20</td>--%>
                <%--							<td>2017-02-07</td>--%>
                <%--							<td>2016-10-20 10:10:10</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--						</tr>--%>
                <%--						<tr>--%>
                <%--							<td>谈判/复审</td>--%>
                <%--							<td>5,000</td>--%>
                <%--							<td>90</td>--%>
                <%--							<td>2017-02-07</td>--%>
                <%--							<td>2017-02-09 10:10:10</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--						</tr>--%>
                </tbody>
            </table>
        </div>

    </div>
</div>

<div style="height: 200px;"></div>

</body>
</html>