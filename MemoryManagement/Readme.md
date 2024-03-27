#    Memory management

## Goal

Develop a memory allocation simulator to allocate
variable-sized partitions of the memory to a given sequence of
processes requests. Apply different allocation policies:

- First-Fit policy.
- Best-Fit policy.
-  Worst-Fit policy.

● Add compaction (as option for the user)

the memory contents so as to place all free memory together in one
large block.

- Input will be as follows:
  - Number of partition
  - Partition name and its size.
  - Number of process requests.
  - Process name and its size.
  - Selected policy by the user
