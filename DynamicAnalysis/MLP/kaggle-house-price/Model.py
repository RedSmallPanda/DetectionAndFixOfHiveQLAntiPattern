import torch
from torch import nn
def get_model(feat_dim):
    net = nn.Sequential(
        nn.Linear(feat_dim, 1)
    )
    return net