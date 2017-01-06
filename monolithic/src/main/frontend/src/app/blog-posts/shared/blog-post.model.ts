export class BlogPostModel {
  id: number;
  title: string;
  renderContent: string;
  publicSlug: string;
  draft: boolean;
  publishAt: Date;
  category: string;
}
