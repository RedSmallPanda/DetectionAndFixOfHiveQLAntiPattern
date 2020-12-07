import torch
from torch import nn

hidden_dim1 = 12
hidden_dim2 = 6


def get_model(feat_dim):
    net = nn.Sequential(
        nn.Linear(feat_dim, hidden_dim1),
        nn.ReLU(inplace=True),
        nn.Linear(hidden_dim1, 1)
    )
    return net
