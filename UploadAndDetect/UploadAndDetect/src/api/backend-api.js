import axios from 'axios'

var baseUrl = "http://localhost:8080/api"

var AXIOS = axios.create({
  baseURL: baseUrl,
  timeout: 1000
});

AXIOS.defaults.headers.post['Content-Type'] = 'application/json';

export default {
  getUsers() {
    console.log("getUsers!")
    return AXIOS.get(`/user/users`)
  }
}
