import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import torch
from torch import nn
from torch.utils.data import DataLoader
from torch.utils.data import TensorDataset
from predict_reduces import pred_reduce_batch
# pred = pd.read_csv('./hive_test_L.csv')
# test = pd.read_csv('./all/joinMlpTrainTest_L.csv')
# figsize = (10, 5)
# fig = plt.figure(figsize=figsize)
# plt.plot(pred['time'], color='red', label='train')
# plt.plot(test['time'], color='blue', label='valid')
# plt.legend(loc='best')
# plt.show()


# def count_group(df):
#     counts=[]
#     count=0
#     tmp_row_val=[0,0,0,0]
#     for row in df.iterrows:
#         if row["t1"]==tmp_row_val[0] & row["key1"]==tmp_row_val[1] & row["t2"]==tmp_row_val[2] & row["key2"]==tmp_row_val[3]:
#             count+=1
#         else:
#             tmp_row_val[0]=row["t1"],tmp_row_val[1]=row["key1"],tmp_row_val[2]=row["t2"],tmp_row_val[3]=row["key2"]
#             counts.append(count)
#             count=1
#     counts.append(count)
#     counts=counts[1:]
#     return counts


#取出数据中 时间最短对应的reduce个数
# results=pd.read_csv('./all/joinMlpTrainData_L.csv')
# results=results.loc[results.groupby(["t1","key1","t2","key2"]).time.idxmin()]
# print(results)
# results.to_csv("./all/joinMLPTrainData_L_mintime.csv")

#batch生成测试数据与真实数据组合，
# df=pd.read_csv('./all/joinMLPTrainData_L_mintime.csv')
# df=pred_reduce_batch(df)
# figsize = (10, 5)
# fig = plt.figure(figsize=figsize)
# plt.plot(df['pred_reduce'], color='red', label='pred')
# plt.plot(df['reduce'], color='blue', label='real')
# plt.legend(loc='best')
# plt.show()
# df.to_csv('./Test_reduce_pred.txt')

#对预测结果和真实数据测试
# test_result=pd.read_csv("./Test_reduce_pred.txt")
# count_all=0
# count_exact=0
# count_time_3s=0
# count_time_2s=0
# count_time_1s=0
# count_num_1=0
# count_num_2=0
# count_num_3=0
# # test_result["pred_time_diff"]=abs(test_result["pred_time"]-test_result["time"])
# # test_result["pred_num_diff"]=abs(test_result["pred_reduce"]-test_result["reduce"])
# for i,row in test_result.iterrows():
#     row=np.array(row)
#     print(row)
#     pred_time=row[-1]
#     pred_reduce=row[-2]
#     real_time=row[-3]
#     real_reduce=row[-4]
#     count_all+=1
#     if pred_reduce==real_reduce:count_exact+=1
#     if abs(pred_time - real_time)<=1000:count_time_1s+=1
#     if abs(pred_time - real_time)<=2000:count_time_2s+=1
#     if abs(pred_time - real_time)<=3000:count_time_3s+=1
#     if abs(pred_reduce-real_reduce<=1):count_num_1+=1
#     if abs(pred_reduce - real_reduce <= 2): count_num_2 += 1
#     if abs(pred_reduce - real_reduce <= 3): count_num_3 += 1
#
# # print(test_result.loc[test_result.pred_reduce.idxmax()])
# print()

#画出某组数据
train_all_df=pd.read_csv("./all/joinMlpTrainData_L.csv")
df=train_all_df[(train_all_df["t1"]==55000) & (train_all_df["key1"]==14) & (train_all_df["t2"]==60000) & (train_all_df["key2"]==3) ]
figsize = (20, 10)
fig = plt.figure(figsize=figsize)
plt.plot(df['time'], color='red', label='train')
plt.legend(loc='best')
plt.show()

#
# pred = pd.read_csv('./train_L.csv')
# test = pd.read_csv('./all/joinMlpTrainTrainData_L_notest.csv')
# figsize = (20, 10)
# fig = plt.figure(figsize=figsize)
# pred_groupby=pred.groupby(["t1","key1","t2","key2"]).agg(["count"])
# test_groupby=test.groupby(["t1","key1","t2","key2"]).agg(["count"])
#
# print(pred_groupby.head(5))
# print(test_groupby.head(5))

# plt.plot(pred['time'], color='red', label='train')
# plt.plot(test['time'], color='blue', label='valid')
# plt.legend(loc='best')
# plt.show()