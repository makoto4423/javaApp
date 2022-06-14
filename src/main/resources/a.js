
function main(){
    return cal(httpRequest())
}

function httpRequest(){
    var a = new Object();
    a.http = 'http';
    a.method = 'method';
    var b = new Object();
    b.mind = 'mind';
    a.spaces = ['Car', 'b', 'without']
    var http = new OkHttp()
    var header = new Object();
    header.token = 'token';
    header.csrf = 'csrf';
    a.body = http.send('POST','http://127.0.0.1:8081/test', header,  JSON.stringify('{"name":"name", "age":10}')).body;
//    a.no = 1
    b = new Object();
    b.body = http.send('POST','http://127.0.0.1:8081/test', header,  JSON.stringify('{"name":"name", "age":10}'));
    return [a,b];
}

function cal(a){
    // a.no = 3;
    return a;
}