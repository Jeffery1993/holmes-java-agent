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
            <div class="wrap">
                <div class="half" v-for="aggregator in monitorData">
                    <div class="view">
                        <el-divider></el-divider>
                        <div v-if="aggregator.type==='table'">
                            <h3>{{aggregator.title}}</h3>
                            <el-table :data="aggregator.data">
                                <el-table-column v-for="(column, index) in aggregator.columns" :key="index"
                                                 :prop="column.prop" :label="column.label"
                                                 align="center"></el-table-column>
                            </el-table>
                        </div>
                        <div v-else-if="aggregator.type==='graph'">
                            <v-chart :options="process(aggregator)"></v-chart>
                        </div>
                    </div>
                </div>
            </div>
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
        created() {
            this.getClusters();
            this.getApps();
            this.getCollectors();
        },
        methods: {
            // 返回正确的格式
            process(aggregator) {
                let legend = [];
                aggregator.dataSeries.forEach((item) => {
                    legend.push(item.name);
                });
                const option = {
                    title: {
                        text: aggregator.title
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: legend
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
                            data: aggregator.timeSeries
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: aggregator.dataSeries
                }
                return option
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
                        this.monitorData = res.data.views;
                    })
                } else {
                    let realUrl = this.monitorUrl;
                    if (this.appId) {
                        realUrl += '?appId=' + this.appId;
                    }
                    this.$axios.get(realUrl).then((res) => {
                        this.monitorData = res.data.views;
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

    .mr10 {
        margin-right: 10px;
    }

    .monitor {
        font-size: 14px;
    }

    .wrap {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
    }

    .half {
        width: 50%;
    }

    .view {
        margin: 10px 10px 10px 10px;
    }
</style>
