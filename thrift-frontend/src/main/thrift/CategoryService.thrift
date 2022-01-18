

service CategoryService{
    public ProductPage GetProductPage(Long prodId);

    public List<CategoryPage> GetCategoryPage(Long userId, String searchKey);

}