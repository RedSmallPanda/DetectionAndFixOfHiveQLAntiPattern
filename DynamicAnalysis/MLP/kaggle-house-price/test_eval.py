import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import torch
from torch import nn
from torch.utils.data import DataLoader
from torch.utils.data import TensorDataset
pred = pd.read_csv('./hive_test2.csv')
test = pd.read_csv('./all/joinMlpTrainTest.csv')
figsize = (10, 5)
fig = plt.figure(figsize=figsize)
plt.plot(pred['time'], color='red', label='train')
plt.plot(test['time'], color='blue', label='valid')
plt.legend(loc='best')
plt.show()