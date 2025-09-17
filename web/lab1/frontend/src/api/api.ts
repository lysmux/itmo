import superagent from "superagent"


export default class ApiClient {
    constructor(
        private readonly apiUrl: string,
    ) {
    }

    get<T>(url: string, query?: Record<string, any>): Promise<T> {
        return new Promise((resolve, reject) => {
            superagent
                .get(`${this.apiUrl}/${url}`)
                .query(query)
                .then(response => {
                    resolve(response.body)
                })
                .catch(error => {
                    reject(error)
                });
        })
    }

    post<T>(url: string, data?: Record<string, any>): Promise<T> {
        return new Promise((resolve, reject) => {
            superagent
                .post(`${this.apiUrl}/${url}`)
                .send(data)
                .then(response => {
                    resolve(response.body)
                })
                .catch(error => {
                    reject(error)
                });
        })
    }

}
