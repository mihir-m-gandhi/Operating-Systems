## File Allocation Strategies - Linked

-----------------------------------------
**Linked Strategy:**

Linked List allocation solves all problems of contiguous allocation. In linked list allocation, each file is considered as the linked list of disk blocks. However, the disks blocks allocated to a particular file need not to be contiguous on the disk. Each disk block allocated to a file contains a pointer which points to the next disk block allocated to the same file.

<p align="center">
    <img src="./example.png">
</p>

------------------------------------------
### Output:

<p align="center">
    <img src="./output/1.png">
</p>

<p align="center">
    <img src="./output/2.png">
</p>

<p align="center">
    <img src="./output/3.png">
</p>

