<template>
    <div class="monitor">
        <div class="container">
            <div class="handle-box">
                <el-button>集群ID</el-button>
                <el-select v-model="clusterId" placeholder="请选择集群" class="handle-select mr10">
                    <el-option key="" label="" value=""></el-option>
                    <el-option v-for="m in clusterIds" :key="m" :label="m" :value="m"></el-option>
                </el-select>
                <el-button>应用ID</el-button>
                <el-select v-model="appId" placeholder="请选择应用" class="handle-select mr10">
                    <el-option v-for="m in appIds" :key="m" :label="m" :value="m"></el-option>
                </el-select>
                <el-button>采集器</el-button>
                <el-select v-model="collector" placeholder="请选择采集器" class="handle-select mr10">
                    <el-option v-for="m in collectors" :key="m" :label="m" :value="m"></el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="getMonitorData">搜索</el-button>
            </div>
            <div v-for="aggregator in monitorData">
                <div v-if="aggregator.table">
                    <el-button type="primary">采集器 {{aggregator.name}}</el-button>
                    <el-table :data="aggregator.table.data" border class="table"
                              ref="multipleTable">
                        <el-table-column v-for="(column, index) in aggregator.table.columns" :key="index" :prop="column"
                                         :label="column" :align="column"></el-table-column>
                    </el-table>
                </div>
                <div v-else-if="aggregator.graph">
                    <el-button type="primary">采集器 {{aggregator.name}}</el-button>
                    <div id="aggregator.name" class="graph"></div>
                </div>
            </div>

            <div id="myChart" class="graph"></div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'monitor',
        data() {
            return {
                // core
                clusterUrl: '/api/monitor/clusters',
                appUrl: '/api/monitor/apps',
                collectorUrl: '/api/monitor/collectors',
                monitorUrl: '/api/monitor/monitorData',
                clusterId: '',
                clusterIds: [],
                appId: '',
                appIds: [],
                collector: '',
                collectors: [],
                monitorData: []
            }
        },
        mounted() {
            this.drawLine();
            this.refreshGraphs();
        },
        created() {
            this.getClusters();
            this.getApps();
            this.getCollectors();
        },
        methods: {
            refreshGraphs() {
                console.log("refreshGraphs")
                for (const aggregator in this.monitorData) {
                    console.log(aggregator)
                    if (aggregator.graph) {
                        console.log(aggregator.graph)
                        this.refreshGraph(aggregator.name, aggregator.graph);
                    }
                }
            },
            refreshGraph(id, graph) {
                console.log(id)
                console.log(document.getElementById(id))
                let chart = this.$echarts.init(document.getElementById(id))
                chart.setOption({
                    title: {
                        text: id
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    // legend: {
                    //     data: ['heapMemoryUsage', 'nonHeapMemoryUsage']
                    // },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: true},
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: graph.xAxis.data
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: graph.yAxis.axisLabel
                            }
                        }
                    ],
                    series: graph.series
                });
            },
            drawLine() {
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '未来一周气温变化'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['最高气温', '最低气温']
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: true},
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: ['2019-06-23 10:00', '2019-06-23 10:02', '2019-06-23 10:04', '2019-06-23 10:06', '2019-06-23 10:08', '2019-06-23 10:10', '2019-06-23 10:12']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value} °C'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '最高气温',
                            type: 'line',
                            data: [11, 11, 15, 13, 12, 13, 10],
                            markPoint: {
                                data: [
                                    {type: 'max', name: '最大值'},
                                    {type: 'min', name: '最小值'}
                                ]
                            }
                        },
                        {
                            name: '最低气温',
                            type: 'line',
                            data: [1, -2, 2, 5, 3, 2, 0],
                            markPoint: {
                                data: [
                                    {name: '周最低', value: -2, xAxis: 1, yAxis: -1.5}
                                ]
                            }
                        }
                    ]
                });
            },
            // 获取cluster数据
            getClusters() {
                // 开发环境使用json假数据
                if (process.env.NODE_ENV === 'development') {
                    let realUrl = './mock/clusters.json';
                    this.$axios.get(realUrl).then((res) => {
                        this.clusterIds = res.data.hits;
                    })
                } else {
                    let realUrl = this.clusterUrl;
                    this.$axios.get(realUrl).then((res) => {
                        this.clusterIds = res.data.hits;
                    })
                }
            },
            // 获取app数据
            getApps() {
                // 开发环境使用json假数据
                if (process.env.NODE_ENV === 'development') {
                    let realUrl = './mock/apps.json';
                    this.$axios.get(realUrl).then((res) => {
                        this.appIds = res.data.hits;
                    })
                } else {
                    let realUrl = this.appUrl;
                    if (this.clusterId) {
                        realUrl += '?clusterId=' + this.clusterId;
                    }
                    this.$axios.get(realUrl).then((res) => {
                        this.appIds = res.data.hits;
                    })
                }
            },
            // 获取collector数据
            getCollectors() {
                // 开发环境使用json假数据
                if (process.env.NODE_ENV === 'development') {
                    let realUrl = './mock/collectors.json';
                    this.$axios.get(realUrl).then((res) => {
                        this.collectors = res.data.hits;
                    })
                } else {
                    let realUrl = this.collectorUrl;
                    this.$axios.get(realUrl).then((res) => {
                        this.collectors = res.data.hits;
                    })
                }
            },
            // 获取监控数据
            getMonitorData() {
                // 开发环境使用json假数据
                if (process.env.NODE_ENV === 'development') {
                    let realUrl = './mock/monitorData.json';
                    this.$axios.get(realUrl).then((res) => {
                        this.monitorData = res.data.hits;
                    })
                } else {
                    let realUrl = this.monitorUrl;
                    this.$axios.get(realUrl).then((res) => {
                        this.monitorData = res.data.hits;
                    })
                }
            }
        }
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .monitor {
        width: 100%;
        font-size: 14px;
    }

    .table {
        width: 50%;
        margin-top: 10px;
        margin-bottom: 20px;
    }

    .graph {
        width: 50%;
        height: 400px;
        margin-top: 10px;
        margin-bottom: 20px;
    }

    .mr10 {
        margin-right: 10px;
    }
</style>
