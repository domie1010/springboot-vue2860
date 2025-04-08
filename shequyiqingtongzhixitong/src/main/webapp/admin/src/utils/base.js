const base = {
    get() {
        return {
            url : "http://localhost:8080/shequyiqingtongzhixitong/",
            name: "shequyiqingtongzhixitong",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/shequyiqingtongzhixitong/front/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "社区疫情通知通告系统"
        } 
    }
}
export default base
