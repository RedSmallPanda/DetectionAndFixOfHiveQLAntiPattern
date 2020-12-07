import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import torch
from torch import nn
from utils import train_model, pred, pred_raw
from Model import get_model

train = pd.read_csv('./all/joinMlpTrainTrainData_notest.csv')
test = pd.read_csv('./all/joinMlpTrainTest.csv')



all_features1 = pd.concat((train.loc[:, 't1':'reduce'],
                          test.loc[:, 't1':'reduce']))

all_features1_mean=all_features1.mean()
all_features1_std=all_features1.std()

t1 = 20000
key1 = 5
t2 = 20000
key2 = 10
test_data = pd.DataFrame(columns=("t1","key1","t2","key2","reduce","time"))
for i in range(1, 12):
    row = {"t1": t1, "key1": key1, "t2": t2, "key2": key2, "reduce": i,"time":-1}
    test_data=test_data.append([row],ignore_index=True)

all_features = test_data.loc[:, 't1':'reduce']

# 减去均值，除以方差
all_features = (all_features-all_features1_mean)/all_features1_std

feat_dim = all_features.shape[1]

test_features = all_features[0:].values.astype(np.float32)
test_features = torch.from_numpy(test_features)

net = torch.load("./hive_pred_model.pkl")

result=pred_raw(net,test_data,test_features)
print(result)

figsize = (10, 5)
fig = plt.figure(figsize=figsize)

plt.plot(result['time'], color='blue', label='pred')
plt.legend(loc='best')
plt.show()

best_reduce=result[result["time"] == result["time"].min()]
print ("最优reduce数：")
print(np.array(best_reduce["reduce"])[0])