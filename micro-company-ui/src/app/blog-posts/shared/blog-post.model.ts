export class BlogPostModel {
  id: number;
  title: string;
  renderContent: string;
  slug: string;
  draft: boolean;
  publishAt: Date;
  category: string;
}
