import fetch from "node-fetch";

class HTTPResponseError extends Error {
    constructor(response) {
		super(`HTTP Error Response: ${response.status} ${response.statusText}`);
		this.response = response;
	}
}

const checkStatus = response => {
	if (response.ok) {
		// response.status >= 200 && response.status < 300
		return response;
	} else {
		throw new HTTPResponseError(response);
	}
}

class APISender {
    /**
     * 보내려는 서버의 주소를 생성자의 인자로 한다.
     * @param {*} protocol 프로토콜 이름
     * @param {*} host 서버 호스트 이름
     * @param {*} port 포트 이름
     */
    constructor(protocol, host, port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }
    /**
     * 서버로 POST 요청을 보내는 동기 함수
     * @param {*} uri 보낼 uri 
     * @param {*} data 보낼 data
     * @returns 응답 받은 데이터
     * @throws {HTTPResponseError}
     */
    async requestTest(uri, data) {
        const response = await fetch(`${this.protocol}://${this.host}:${this.port}/middleSpace/${uri}`, {
            method: 'post',
            body : JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        checkStatus(response);
        return await response.json();
    }
};

export {
    APISender,
    HTTPResponseError
}