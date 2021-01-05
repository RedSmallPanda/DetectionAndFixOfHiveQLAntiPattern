import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import torch
from torch import nn
from utils import train_model, pred, pred_raw
from Model import get_model


# train = pd.read_csv('./all/joinMlpTrainTrainData_notest.csv')
# test = pd.read_csv('./all/joinMlpTrainTest.csv')
#
#
#
# all_features1 = pd.concat((train.loc[:, 't1':'reduce'],
#                           test.loc[:, 't1':'reduce']))
#
# all_features1_mean=all_features1.mean()
# all_features1_std=all_features1.std()
#
# t1 = 5000
# key1 = 4
# t2 = 200000
# key2 = 4
# test_data = pd.DataFrame(columns=("t1","key1","t2","key2","reduce","time"))
# for i in range(1, 12):
#     row = {"t1": t1, "key1": key1, "t2": t2, "key2": key2, "reduce": i,"time":-1}
#     test_data=test_data.append([row],ignore_index=True)
#
# all_features = test_data.loc[:, 't1':'reduce']
#
# # 减去均值，除以方差
# all_features = (all_features-all_features1_mean)/all_features1_std
#
# feat_dim = all_features.shape[1]
#
# test_features = all_features[0:].values.astype(np.float32)
# test_features = torch.from_numpy(test_features)
#
# net = torch.load("./hive_pred_model2.pkl")
#
# result=pred_raw(net,test_data,test_features)
# print(result)
#
# figsize = (10, 5)
# fig = plt.figure(figsize=figsize)
#
# plt.plot(result['time'], color='blue', label='pred')
# plt.legend(loc='best')
# plt.show()
#
# best_reduce=result[result["time"] == result["time"].min()]
# print ("最优reduce数：")
# print(np.array(best_reduce["reduce"])[0])
def get_normalize_param():
    train = pd.read_csv('./all/joinMlpTrainTrainData_L_notest.csv')
    test = pd.read_csv('./all/joinMlpTrainTest_L.csv')

    all_features1 = pd.concat((train.loc[:, 't1':'reduce'],
                               test.loc[:, 't1':'reduce']))

    all_features1_mean = all_features1.mean()
    all_features1_std = all_features1.std()
    return all_features1_mean, all_features1_std


def pred_reduce_single(t1, key1, t2, key2, mean, std):
    test_data = pd.DataFrame(columns=("t1", "key1", "t2", "key2", "reduce", "time"))
    for i in range(1, 20):
        row = {"t1": t1, "key1": key1, "t2": t2, "key2": key2, "reduce": i, "time": -1}
        test_data = test_data.append([row], ignore_index=True)

    all_features = test_data.loc[:, 't1':'reduce']

    # 减去均值，除以方差
    all_features = (all_features - mean) / std

    feat_dim = all_features.shape[1]

    test_features = all_features[0:].values.astype(np.float32)
    test_features = torch.from_numpy(test_features)

    net = torch.load("./hive_pred_model_L.pkl")

    result = pred_raw(net, test_data, test_features)
    best_reduce = result[result["time"] == result["time"].min()]

    return np.array(best_reduce["reduce"])[0]


def pred_reduce(t1, key1, t2, key2):
    train = pd.read_csv('./all/joinMlpTrainTrainData_L_notest.csv')
    test = pd.read_csv('./all/joinMlpTrainTest_L.csv')

    all_features1 = pd.concat((train.loc[:, 't1':'reduce'],
                               test.loc[:, 't1':'reduce']))

    all_features1_mean = all_features1.mean()
    all_features1_std = all_features1.std()
    test_data = pd.DataFrame(columns=("t1", "key1", "t2", "key2", "reduce", "time"))
    for i in range(1, 20):
        row = {"t1": t1, "key1": key1, "t2": t2, "key2": key2, "reduce": i, "time": -1}
        test_data = test_data.append([row], ignore_index=True)

    all_features = test_data.loc[:, 't1':'reduce']

    # 减去均值，除以方差
    all_features = (all_features - all_features1_mean) / all_features1_std

    feat_dim = all_features.shape[1]

    test_features = all_features[0:].values.astype(np.float32)
    test_features = torch.from_numpy(test_features)

    net = torch.load("./hive_pred_model_L.pkl")

    result = pred_raw(net, test_data, test_features)
    best_reduce = result[result["time"] == result["time"].min()]
    print("最优reduce数：")
    print(np.array(best_reduce["reduce"])[0])

    # figsize = (10, 5)
    # fig = plt.figure(figsize=figsize)
    #
    # plt.plot(result['time'], color='blue', label='pred')
    # plt.legend(loc='best')
    # plt.show()
    return np.array(best_reduce["reduce"])[0]


def pred_reduce_batch(df):
    train_all_df=pd.read_csv("./all/joinMlpTrainData_L.csv")
    all_features1_mean, all_features1_std = get_normalize_param()
    df["pred_reduce"] = df["reduce"] * 0
    df["pred_time"] = df["time"] * 0
    print(df)
    for i, row in df.iterrows():
        row=np.array(row)
        print(row.shape)
        t1 = row[0]
        key1 = row[1]
        t2 = row[2]
        key2 = row[3]
        print(t1,key1,t2,key2)
        pred_reduce_num = pred_reduce_single(t1, key1, t2, key2, all_features1_mean, all_features1_std)
        print("reduce pred:")
        print(pred_reduce_num)
        df.iloc[i]["pred_reduce"] = pred_reduce_num
        # print(train_all_df)
        df.iloc[i]["pred_time"]=train_all_df[(train_all_df["t1"]==t1) & (train_all_df["key1"]==key1) & (train_all_df["t2"]==t2) & (train_all_df["key2"]==key2) & (train_all_df["reduce"]==pred_reduce_num)].iloc[0]["time"]
        # print(np.array(train_all_df[(train_all_df["t1"]==t1) & (train_all_df["key1"]==key1) & (train_all_df["t2"]==t2) & (train_all_df["key2"]==key2) & (train_all_df["reduce"]==pred_reduce_num)].iloc[0]["time"]))
    print(df)

    return df
