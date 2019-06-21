<template>
    <div class="table">
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
            <div class="table" v-for="aggregator in monitorData">
                <el-button type="primary">采集器 {{aggregator.name}}</el-button>
                <el-table :data="aggregator.data" border class="table"
                          ref="multipleTable">
                    <el-table-column v-for="(column, index) in aggregator.columns" :key="index" :prop="column"
                                     :label="column" :align="column"></el-table-column>
                </el-table>
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
                monitorData: {}
            }
        },
        created() {
            this.getClusters();
            this.getApps();
            this.getCollectors();
        },
        methods: {
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
                        console.log(res.data)
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

    .table {
        width: 100%;
        font-size: 14px;
        margin-top: 10px;
        margin-bottom: 20px;
    }

    .mr10 {
        margin-right: 10px;
    }
</style>
