
service CartService{
 /*
    * Big Data Transfer
    * */
    List<CartInfo> GetCartInfosByAccount(Long accountId);

    List<CartInfo> GetCartInfosByAccount(Long accountId,Integer upperLimit);

}