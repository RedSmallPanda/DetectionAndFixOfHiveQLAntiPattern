import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import torch
from torch import nn
from torch.utils.data import DataLoader
from torch.utils.data import TensorDataset
# pred = pd.read_csv('./hive_test2.csv')
# test = pd.read_csv('./all/joinMlpTrainTest.csv')
# figsize = (10, 5)
# fig = plt.figure(figsize=figsize)
# plt.plot(pred['time'], color='red', label='train')
# plt.plot(test['time'], color='blue', label='valid')
# plt.legend(loc='best')
# plt.show()
def count_group(df):
    counts=[]
    count=0
    tmp_row_val=[0,0,0,0]
    for row in df.iterrows:
        if row["t1"]==tmp_row_val[0] & row["key1"]==tmp_row_val[1] & row["t2"]==tmp_row_val[2] & row["key2"]==tmp_row_val[3]:
            count+=1
        else:
            tmp_row_val[0]=row["t1"],tmp_row_val[1]=row["key1"],tmp_row_val[2]=row["t2"],tmp_row_val[3]=row["key2"]
            counts.append(count)
            count=1
    counts.append(count)
    counts=counts[1:]
    return counts
pred = pd.read_csv('./train.csv')
test = pd.read_csv('./all/joinMlpTrainTrainData_notest.csv')
figsize = (20, 10)
fig = plt.figure(figsize=figsize)
pred_groupby=pred.groupby(["t1","key1","t2","key2"]).agg(["count"])
test_groupby=test.groupby(["t1","key1","t2","key2"]).agg(["count"])

print(pred_groupby.head(5))
print(test_groupby.head(5))

plt.plot(pred[20:50]['time'], color='red', label='train')
plt.plot(test[20:50]['time'], color='blue', label='valid')
plt.legend(loc='best')
plt.show()