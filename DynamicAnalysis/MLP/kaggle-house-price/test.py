import torch
import pandas
def main():
    print("hello")
    print(torch.__version__)
    x_gpu=torch.Tensor([3]).cuda()
    print(x_gpu)


if __name__ == '__main__':
    main()
