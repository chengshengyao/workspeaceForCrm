<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath() + "/"; %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title></title>
    <script src="EChars/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script>
        $(function () {
            getEchars();


        })

        function getEchars() {
            //验证 账号密码不为空的情况下 在【后台】数据库中进行 数据比对 [异步请求 局部刷新]
            $.ajax({
                url:"/workbench/transaction/getEchars.do",
                type:"get",
                dataType:"json",
                success:function (data) {
                    // 指定图表的配置项和数据
                    option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易阶段数量'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: [
                                '01资质审查'
                                ,
                                '02需求分析'
                                ,
                                '03价值建议'
                                ,
                                '04确定决策者'
                                ,
                                '05提案/报价'
                                ,
                                '06谈判/复审'
                                ,
                                '07成交'
                                ,
                                '08丢失的线索'
                                ,
                                '09因竞争丢失关闭'
                            ]
                        }

                        ,

                        series: [
                            {
                                name: '漏斗图',
                                type: 'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: [
                                    // {value: 60, name: '01资质审查'},
                                    // {value: 40, name: '02需求分析'},
                                    // {value: 20, name: '03价值建议'},
                                    // {value: 80, name: '04确定决策者'},
                                    // {value: 100, name: '05提案/报价'},
                                    // {value: 100, name: '06谈判/复审'},
                                    // {value: 100, name: '07成交'},
                                    // {value: 100, name: '08丢失的线索'},
                                    // {value: 100, name: '09因竞争丢失关闭'}
                                    data.dataList
                                ]
                            }
                        ]
                    }
                    ;
                }
            })
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    </script>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>