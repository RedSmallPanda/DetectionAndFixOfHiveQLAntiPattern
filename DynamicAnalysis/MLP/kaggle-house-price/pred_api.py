from flask import Flask, request
from predict_reduces import pred_reduce

app = Flask(__name__)


@app.route('/predict_reduce/<int:t1>/<int:key1>/<int:t2>/<int:key2>')
def predict_reduce(t1,key1,t2,key2):
    print(t1,key1,t2,key2)
    reduce_num = pred_reduce(t1, key1, t2, key2)
    return str(reduce_num)


if __name__ == '__main__':
    app.run(host="0.0.0.0",port=50009)
