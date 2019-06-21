<template>
    <div class="table">
        <div class="container">
            <div class="handle-box">
                <el-button>集群ID</el-button>
                <el-select v-model="clusterId" placeholder="可选" class="handle-select mr10">
                    <el-option key="" label="" value=""></el-option>
                    <el-option v-for="m in clusterIds" :key="m" :label="m" :value="m"></el-option>
                </el-select>
                <el-button>应用ID</el-button>
                <el-select v-model="appId" placeholder="可选" class="handle-select mr10">
                    <el-option key="" label="" value=""></el-option>
                    <el-option v-for="m in appIds" :key="m" :label="m" :value="m"></el-option>
                </el-select>
                <el-button>调用链id</el-button>
                <el-input v-model="traceId" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="getSpanData">搜索</el-button>
            </div>
            <el-table :data="spanData" border class="table" ref="multipleTable">
                <el-table-column prop="traceId" label="traceId" align="center"></el-table-column>
                <el-table-column prop="url" label="url" align="center"></el-table-column>
                <el-table-column prop="method" label="method" align="center"></el-table-column>
                <el-table-column prop="startTime" label="开始时间" sortable align="center"></el-table-column>
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-view" @click="showDetails(scope.$index)">查看
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        @size-change="handleSizeChange"
                        background @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-sizes="[5, 10, 20, 50, 100]"
                        :page-size="10"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="totalSize">
                </el-pagination>
            </div>
        </div>

        <!-- 查看调用链详情弹出框 -->
        <el-dialog :title="selectedTraceId" :visible.sync="detailsVisible" width="80%">
            <el-table :data="spanEventData" border class="table" ref="multipleTable">
                <el-table-column prop="className" label="类名" width="350" align="center"></el-table-column>
                <el-table-column prop="methodName" label="方法名" align="center"></el-table-column>
                <el-table-column prop="eventType" label="类型" align="center"></el-table-column>
                <el-table-column prop="spanId" label="spanId" sortable align="center"></el-table-column>
                <el-table-column prop="spanEventId" label="spanEventId" sortable align="center"></el-table-column>
                <el-table-column prop="startTime" label="开始时间" sortable align="center"></el-table-column>
                <el-table-column prop="usedTime" label="耗时" sortable align="center"></el-table-column>
            </el-table>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'trace',
        data() {
            return {
                // common
                clusterUrl: '/api/trace/clusters',
                appUrl: '/api/trace/apps',
                // span
                spanRootUrl: '/api/trace/spans',
                clusterId: '',
                clusterIds: [],
                appId: '',
                appIds: [],
                traceId: '',
                spanData: [],
                currentPage: 1,
                pageSize: 10,
                totalSize: 0,
                // span event
                spanEventRootUrl: '/api/trace/spanEvents',
                spanEventData: [],
                selectedTraceId: '',
                detailsVisible: false
            }
        },
        created() {
            this.getClusters();
            this.getApps();
            this.getSpanData();
        },
        computed: {
            spanData() {
                return this.spanData;
            }
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
            // 获取span数据
            getSpanData() {
                // 开发环境使用json假数据
                if (process.env.NODE_ENV === 'development') {
                    let realUrl = './mock/spans.json';
                    this.$axios.get(realUrl).then((res) => {
                        this.spanData = res.data.hits;
                        this.totalSize = res.data.total;
                    })
                } else {
                    let realUrl = this.spanRootUrl + '?page=' + this.currentPage + '&pageSize=' + this.pageSize;
                    if (this.clusterId) {
                        realUrl += '&clusterId=' + this.clusterId;
                    }
                    if (this.appId) {
                        realUrl += '&appId=' + this.appId;
                    }
                    if (this.traceId) {
                        realUrl += '&traceId=' + this.traceId;
                    }
                    this.$axios.get(realUrl).then((res) => {
                        this.spanData = res.data.hits;
                        this.totalSize = res.data.total;
                    })
                }
            },
            // 分页导航
            handleSizeChange(val) {
                this.pageSize = val;
                this.getSpanData();
            },
            handleCurrentChange(val) {
                this.currentPage = val;
                this.getSpanData();
            },
            // 展示详情
            showDetails(index) {
                this.idx = index;
                this.selectedTraceId = this.spanData[index].traceId;
                this.getSpanEventData(this.selectedTraceId);
                this.detailsVisible = true;
            },
            // 获取spanEvent数据
            getSpanEventData(traceId) {
                // 开发环境使用json假数据
                if (process.env.NODE_ENV === 'development') {
                    let realUrl = './mock/spanEvents.json';
                    this.$axios.get(realUrl).then((res) => {
                        this.spanEventData = res.data.hits;
                    })
                } else {
                    let realUrl = this.spanEventRootUrl + '?traceId=' + traceId;
                    this.$axios.get(realUrl).then((res) => {
                        this.spanEventData = res.data.hits;
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

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .mr10 {
        margin-right: 10px;
    }
</style>
